package nl.windesheim.codeparser.analyzers.observer.componentfinders;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.components.ObserverCollection;

import java.util.*;

public class SubscriptionMethodFinder extends ObservableMethodFinder {
    private Map<ObserverCollection, List<EligibleSubscriptionParameter>> eligibleParameters;

    public SubscriptionMethodFinder (final TypeSolver typeSolver, List<ObserverCollection> observerCollections) {
        super(typeSolver, observerCollections);
        eligibleParameters = new HashMap<>();
    }

    @Override
    public void determine (final MethodDeclaration methodDeclaration) {
        // Does the method take parameters of the type of one of the eligible collections?
        eligibleParameters = this.findCollectionParameters(methodDeclaration, observerCollections);

        if (!eligibleParameters.isEmpty()) {
            // Does it contain a method declaration with one of the eligible collections as scope?
            findSubscriptionMethodCalls(methodDeclaration);
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

    private void findSubscriptionMethodCalls (final MethodDeclaration methodDeclaration) {
        List<MethodCallExpr> methodCalls = methodDeclaration.findAll(MethodCallExpr.class);
        for (MethodCallExpr methodCall : methodCalls) {
            Optional<Expression> optionalScope = methodCall.getScope();
            if (!optionalScope.isPresent()) {
                continue;
            }

            Expression scopeExpression = optionalScope.get();
            ResolvedValueDeclaration scope;
            if (scopeExpression.isNameExpr()) {
                scope = JavaParserFacade.get(typeSolver).solve(scopeExpression).getCorrespondingDeclaration();
            } else if (scopeExpression.isFieldAccessExpr()) {
                scope = scopeExpression.asFieldAccessExpr().resolve();
            } else {
                continue;
            }

            JavaParserFieldDeclaration scopeSymbol = (JavaParserFieldDeclaration) scope;
            if (scopeSymbol.getWrappedNode() == null) {
                continue;
            }

            FieldDeclaration scopeFieldDeclaration = scopeSymbol.getWrappedNode().asFieldDeclaration();
            ResolvedValueDeclaration resolvedScopeFieldDeclaration = null;
            for (VariableDeclarator fieldVariable : scopeFieldDeclaration.getVariables()) {
                if (fieldVariable.getNameAsString().equals(scope.getName())) {
                    resolvedScopeFieldDeclaration = fieldVariable.resolve();
                }
            }

            // Does that method declaration operate on the add or remove method of the collection type?
            ObserverCollection operatesOn = null;
            for (ObserverCollection observerCollection : observerCollections) {
                ResolvedFieldDeclaration resolvedFieldDeclaration = observerCollection.getVariableDeclarator().resolve();

                if (resolvedScopeFieldDeclaration.getName().equals(observerCollection.getVariableDeclarator().resolve().getName())) {
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

    private class EligibleSubscriptionParameter {
        private Parameter parameter;
        private ResolvedReferenceType resolvedReferenceType;
        private String parameterName;
    }
}
