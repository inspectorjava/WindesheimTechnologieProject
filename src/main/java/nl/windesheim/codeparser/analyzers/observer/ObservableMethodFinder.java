package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import java.util.*;

public class ObservableMethodFinder {
    private TypeSolver typeSolver;

    private List<ObserverCollection> observerCollections;
    private Map<ObserverCollection, List<EligibleSubscriptionParameter>> eligibleParameters;

    public ObservableMethodFinder(TypeSolver typeSolver, List<ObserverCollection> observerCollections) {
        this.typeSolver = typeSolver;
        this.observerCollections = observerCollections;
        this.eligibleParameters = new HashMap<>();
    }

    public void findObservableMethods (final ClassOrInterfaceDeclaration classDeclaration) {
        // TODO The method should be public (or protected/package access?)
        // Find all methods operating on the found collections

        if (!classDeclaration.isInterface()) {
            List<MethodDeclaration> methodDeclarations = classDeclaration.findAll(MethodDeclaration.class);
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                determineSubscriptionMethod(methodDeclaration);
                determineNotificationMethod(methodDeclaration);
            }
        }
    }

    // TODO Verhuizen naar een aparte klasse
    private void determineNotificationMethod (final MethodDeclaration methodDeclaration) {
        // A notify method is a method that loops over an eligible collection, and calls a method
        // on the elements in the collection
        // The method should be public (or protected/package access?)

        // Check if the method loops over an eligible collection
        // The method contains a for, foreach or while
        List<ForeachStmt> foreachStatements = methodDeclaration.findAll(ForeachStmt.class);

        for (ForeachStmt foreachStatement : foreachStatements) {
            ObserverCollection operatesOn = findEligibleForeachStatement(foreachStatement);

            if (operatesOn == null) {
                continue;
            }

            // Check of foreachstatement opereert op een methode van het 'geitereerde' element
            VariableDeclarator foreachVariable = foreachStatement.getVariable().getVariable(0);

            Statement foreachBodyStatement = foreachStatement.getBody();
            List<MethodCallExpr> methodCalls = new ArrayList<>();
            if (foreachBodyStatement.isBlockStmt()) {
                BlockStmt foreachBodyBlock = foreachBodyStatement.asBlockStmt();
                methodCalls = foreachBodyBlock.findAll(MethodCallExpr.class);
            } else if (foreachBodyStatement.isExpressionStmt()) {
                ExpressionStmt foreachBodyExpression = foreachBodyStatement.asExpressionStmt();

                if (foreachBodyExpression.getExpression().isMethodCallExpr()) {
                    methodCalls.add(foreachBodyExpression.getExpression().asMethodCallExpr());
                }
            }

            if (methodCalls.isEmpty()) {
                continue;
            }

            // Check of het een methodeaanroep is op de foreachVariable
            for (MethodCallExpr methodCall : methodCalls) {
                Optional<Expression> optionalMethodScope = methodCall.getScope();

                if (optionalMethodScope.isPresent() && optionalMethodScope.get().isNameExpr()) {
                    NameExpr scopeExpression = optionalMethodScope.get().asNameExpr();
                    ResolvedValueDeclaration scope = JavaParserFacade.get(typeSolver).solve(scopeExpression).getCorrespondingDeclaration();

                    if (scope instanceof JavaParserSymbolDeclaration) {
                        JavaParserSymbolDeclaration scopeSymbol = (JavaParserSymbolDeclaration) scope;

                        if (scopeSymbol.getWrappedNode() instanceof VariableDeclarator) {
                            VariableDeclarator scopeVariable = (VariableDeclarator) scopeSymbol.getWrappedNode();

                            if (foreachVariable.equals(scopeVariable)) {
                                // TODO Bewaar welke methode er wordt aangeroepen, dat is dan de update-methode
                                // TODO Extra gecheck?
                                operatesOn.addNotifyMethod(methodDeclaration);
                            }
                        }
                    }
                }
            }
        }
    }

    private ObserverCollection findEligibleForeachStatement (final ForeachStmt foreachStatement) {
        Expression rawIterable = foreachStatement.getIterable();
        if (rawIterable.isNameExpr()) {
            NameExpr iterable = rawIterable.asNameExpr();
            ResolvedValueDeclaration iterableValue = JavaParserFacade.get(typeSolver).solve(iterable).getCorrespondingDeclaration();

            // Check of iterableValue verwijst naar een property (JavaParserFieldDeclaration)
            if (iterableValue instanceof JavaParserFieldDeclaration) {
                JavaParserFieldDeclaration iterableField = (JavaParserFieldDeclaration) iterableValue;
                for (ObserverCollection collection : observerCollections) {
                    if (collection.getVariableDeclarator().getNameAsString().equals(iterableField.getName())) {
                        return collection;
                    }
                }
            }
        }

        return null;
    }

    // TODO Verhuizen naar een aparte klasse
    private void determineSubscriptionMethod (final MethodDeclaration methodDeclaration) {
        // TODO Boolean teruggeven: zo niet, dan hoeft er niet verder gekeken te worden
        // Does the method take parameters of the type of one of the eligible collections?
        eligibleParameters = this.findCollectionParameters(methodDeclaration, observerCollections);

        if (!eligibleParameters.isEmpty()) {
            // Does it contain a method declaration with one of the eligible collections as scope?
            findSubscriptionMethodCalls(methodDeclaration);
        }
    }

    private void findSubscriptionMethodCalls (final MethodDeclaration methodDeclaration) {
        List<MethodCallExpr> methodCalls = methodDeclaration.findAll(MethodCallExpr.class);
        for (MethodCallExpr methodCall : methodCalls) {
            Optional<Expression> optionalScope = methodCall.getScope();
            if (!optionalScope.isPresent()) {
                continue;
            }

            Expression scopeExpression = optionalScope.get();

            if (!scopeExpression.isFieldAccessExpr()) {
                continue;
            }

            // Does that method declaration operate on the add or remove method of the collection type?
            ObserverCollection operatesOn = null;
            FieldAccessExpr fieldAccessExpression = scopeExpression.asFieldAccessExpr();
            for (ObserverCollection observerCollection : observerCollections) {
                if (fieldAccessExpression.getNameAsString().equals(observerCollection.getVariableDeclarator().getNameAsString())) {
                    operatesOn = observerCollection;
                }
            }

            if (operatesOn == null) {
                continue;
            }

            // Check of het de add- of remove-methode is
            ResolvedMethodDeclaration resolvedMethodDeclaration = JavaParserFacade.get(typeSolver).solve(methodCall).getCorrespondingDeclaration();
            String methodDeclarationSignature = resolvedMethodDeclaration.getQualifiedSignature();
            String collectionTypeSignature = operatesOn.getFieldType().getQualifiedName();

            boolean possibleAttach = methodDeclarationSignature.equals(collectionTypeSignature + ".add(E)");
            boolean possibleDetach = !possibleAttach && methodDeclarationSignature.equals(collectionTypeSignature + ".remove(java.lang.Object)");

            if (!possibleAttach && !possibleDetach) {
                continue;
            }

            // Check whether it receives an eligible parameter
            // TODO The parameter may have been reassigned to another variable in the mean time
            NameExpr argumentExpression = methodCall.getArgument(0).asNameExpr();
            String argumentName = argumentExpression.getNameAsString();

            boolean passesParameter = false;
            List<EligibleSubscriptionParameter> possibleParameters = eligibleParameters.get(operatesOn);
            for (EligibleSubscriptionParameter parameter : possibleParameters) {
                if (parameter.parameter.getNameAsString().equals(argumentName)) {
                    passesParameter = true;
                    break;
                }
            }

            if (passesParameter) {
                if (possibleAttach) {
                    operatesOn.addAttachMethod(methodDeclaration);
                } else {
                    operatesOn.addDetachMethod(methodDeclaration);
                }
            }
        }
    }

    private Map<ObserverCollection, List<EligibleSubscriptionParameter>> findCollectionParameters (final MethodDeclaration methodDeclaration, final List<ObserverCollection> observerCollections) {
        NodeList<Parameter> parameters = methodDeclaration.getParameters();
        Map<ObserverCollection, List<EligibleSubscriptionParameter>> eligibleParameters = new HashMap<>();

        for (Parameter parameter : parameters) {
            ResolvedType parameterType = parameter.getType().resolve();
            if (parameterType.isReferenceType()) {
                ResolvedReferenceType parameterReferenceType = (ResolvedReferenceType) parameterType;
                String qualifiedName = ((ResolvedReferenceType)parameterType).getQualifiedName();

                EligibleSubscriptionParameter eligibleParameter = new EligibleSubscriptionParameter();
                eligibleParameter.parameter = parameter;
                eligibleParameter.resolvedReferenceType = parameterReferenceType;

                for (ObserverCollection observerCollection : observerCollections) {
                    if (parameterReferenceType.getQualifiedName().equals(observerCollection.getParameterType().getQualifiedName())) {
                        if (eligibleParameters.keySet().contains(observerCollection)) {
                            eligibleParameters.get(observerCollection).add(eligibleParameter);
                        } else {
                            List<EligibleSubscriptionParameter> parameterList = new ArrayList<>();
                            parameterList.add(eligibleParameter);
                            eligibleParameters.put(observerCollection, parameterList);
                        }
                    }
                }
            }
        }

        return eligibleParameters;
    }

    private class EligibleSubscriptionParameter {
        private Parameter parameter;
        private ResolvedReferenceType resolvedReferenceType;
        private String parameterName;
    }
}
