package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import java.util.*;

public class ObservableMethodFinder {
    private TypeSolver typeSolver;

    private List<EligibleCollection> eligibleCollections;
    private Map<EligibleCollection, List<EligibleSubscriptionParameter>> eligibleParameters;

    public ObservableMethodFinder(TypeSolver typeSolver, List<EligibleCollection> eligibleCollections) {
        this.typeSolver = typeSolver;
        this.eligibleCollections = eligibleCollections;
        this.eligibleParameters = new HashMap<>();
    }

    public void findObservableMethods (final ClassOrInterfaceDeclaration classDeclaration) {
        // TODO The method should be public (or protected/package access?)
        // Find all methods operating on the found collections

        if (!classDeclaration.isInterface()) {
            List<MethodDeclaration> methodDeclarations = classDeclaration.findAll(MethodDeclaration.class);
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                determineSubscriptionMethod(methodDeclaration);

                // TODO Implement
                // A notify method is a method that loops over an eligible collection, and calls a method
                // on the elements in the collection
                // The method should be public (or protected/package access?)
            }
        }
    }

    private void determineSubscriptionMethod (final MethodDeclaration methodDeclaration) {
        // Does the method take parameters of the type of one of the eligible collections?
        eligibleParameters = this.findCollectionParameters(methodDeclaration, eligibleCollections);

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
            EligibleCollection operatesOn = null;
            FieldAccessExpr fieldAccessExpression = scopeExpression.asFieldAccessExpr();
            for (EligibleCollection eligibleCollection : eligibleCollections) {
                if (fieldAccessExpression.getNameAsString().equals(eligibleCollection.getVariableDeclarator().getNameAsString())) {
                    operatesOn = eligibleCollection;
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

    private Map<EligibleCollection, List<EligibleSubscriptionParameter>> findCollectionParameters (final MethodDeclaration methodDeclaration, final List<EligibleCollection> eligibleCollections) {
        NodeList<Parameter> parameters = methodDeclaration.getParameters();
        Map<EligibleCollection, List<EligibleSubscriptionParameter>> eligibleParameters = new HashMap<>();

        for (Parameter parameter : parameters) {
            ResolvedType parameterType = parameter.getType().resolve();
            if (parameterType.isReferenceType()) {
                ResolvedReferenceType parameterReferenceType = (ResolvedReferenceType) parameterType;
                String qualifiedName = ((ResolvedReferenceType)parameterType).getQualifiedName();

                EligibleSubscriptionParameter eligibleParameter = new EligibleSubscriptionParameter();
                eligibleParameter.parameter = parameter;
                eligibleParameter.resolvedReferenceType = parameterReferenceType;

                for (EligibleCollection eligibleCollection : eligibleCollections) {
                    if (parameterReferenceType.getQualifiedName().equals(eligibleCollection.getParameterType().getQualifiedName())) {
                        if (eligibleParameters.keySet().contains(eligibleCollection)) {
                            eligibleParameters.get(eligibleCollection).add(eligibleParameter);
                        } else {
                            List<EligibleSubscriptionParameter> parameterList = new ArrayList<>();
                            parameterList.add(eligibleParameter);
                            eligibleParameters.put(eligibleCollection, parameterList);
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
