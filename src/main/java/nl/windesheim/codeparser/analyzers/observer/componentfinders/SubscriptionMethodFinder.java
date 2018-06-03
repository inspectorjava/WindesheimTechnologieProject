package nl.windesheim.codeparser.analyzers.observer.componentfinders;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.sun.tools.javac.comp.Resolve;
import nl.windesheim.codeparser.analyzers.observer.components.ObserverCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Finds subscription methods for a potential AbstractObservable.
 *
 * A subscription method adheres to the following criteria:
 * - It operates on a potential observer collection
 * - It performs an add or remove operation on the observer collection
 * - The method is being passed an element to add to or remove from the collection
 */
public class SubscriptionMethodFinder extends ObservableMethodFinder {
    /**
     * A map linking potential observer collections to found method parameters.
     */
    private Map<ObserverCollection, List<EligibleSubscriptionParameter>> eligibleParams;

    /**
     * SubscriptionMethodFinder constructor.
     *
     * @param typeSolver          A TypeSolver which can be used by this class
     * @param observerCols A list of detected potential observer collections
     */
    public SubscriptionMethodFinder(final TypeSolver typeSolver, final List<ObserverCollection> observerCols) {
        super(typeSolver, observerCols);
        eligibleParams = new HashMap<>();
    }

    @Override
    public void determine(final MethodDeclaration method) {
        // Does the method take parameters of the type of one of the eligible collections?
        eligibleParams = this.findCollectionParameters(method, getObserverCollections());

        if (!eligibleParams.isEmpty()) {
            // Does it contain a method declaration with one of the eligible collections as scope?
            findSubscriptionMethodCalls(method);
        }
    }

    /**
     * Maps which observer collections can be operated on, given a set of method parameters.
     *
     * @param methodDeclaration   The method being analyzed
     * @param observerCols A list of potential observer collections
     * @return A map linking potential observer collections to parameters of interest, of the given method
     */
    private Map<ObserverCollection, List<EligibleSubscriptionParameter>> findCollectionParameters(
            final MethodDeclaration methodDeclaration,
            final List<ObserverCollection> observerCols) {
        NodeList<Parameter> parameters = methodDeclaration.getParameters();
        Map<ObserverCollection, List<EligibleSubscriptionParameter>> eligibleParams = new HashMap<>();

        for (Parameter parameter : parameters) {
            try {
                ResolvedType paramType = parameter.getType().resolve();
                if (paramType.isReferenceType()) {
                    ResolvedReferenceType paramRefType = (ResolvedReferenceType) paramType;

                    EligibleSubscriptionParameter eligibleParameter = new EligibleSubscriptionParameter();
                    eligibleParameter.setParameter(parameter);
                    eligibleParameter.setResolvedReferenceType(paramRefType);

                    for (ObserverCollection observerCol : observerCols) {
                        if (paramRefType.getQualifiedName().equals(
                                observerCol.getParameterType().getQualifiedName())) {
                            if (eligibleParams.keySet().contains(observerCol)) {
                                eligibleParams.get(observerCol).add(eligibleParameter);
                            } else {
                                List<EligibleSubscriptionParameter> parameterList = new ArrayList<>();
                                parameterList.add(eligibleParameter);
                                eligibleParams.put(observerCol, parameterList);
                            }
                        }
                    }
                }
            } catch (UnsolvedSymbolException ex) {
                // FIXME Fix exception log
            }
        }

        return eligibleParams;
    }

    /**
     * Finds indicators which hint at the given method being a subscription method.
     *
     * @param methodDeclaration The method being analyzed
     */
    private void findSubscriptionMethodCalls(final MethodDeclaration methodDeclaration) {
        List<MethodCallExpr> methodCalls = methodDeclaration.findAll(MethodCallExpr.class);
        for (MethodCallExpr methodCall : methodCalls) {
            try {
                ResolvedValueDeclaration resScopeField = getResolvedScopeField (methodCall);
                if (resScopeField == null) {
                    continue;
                }

                // Does that method declaration operate on the add or remove method of the collection type?
                ObserverCollection operatesOn = null;
                for (ObserverCollection observerCol : getObserverCollections()) {
                    if (resScopeField.getName().equals(
                            observerCol.getVariableDeclarator().resolve().getName())) {
                        operatesOn = observerCol;
                    }
                }

                if (operatesOn == null) {
                    continue;
                }

                // Check of het de add- of remove-methode is
                ResolvedMethodDeclaration resMethod =
                        JavaParserFacade.get(getTypeSolver()).solve(methodCall).getCorrespondingDeclaration();
                String methodSignature = resMethod.getQualifiedSignature();
                String colTypeSignature = operatesOn.getFieldType().getQualifiedName();

                boolean possibleAttach = methodSignature.equals(colTypeSignature + ".add(E)");
                boolean possibleDetach =
                        !possibleAttach && methodSignature.equals(
                                colTypeSignature + ".remove(java.lang.Object)");

                if (!possibleAttach && !possibleDetach) {
                    continue;
                }

                // Check whether it receives an eligible parameter
                // TODO The parameter may have been reassigned to another variable in the mean time
                NameExpr argumentExpr = methodCall.getArgument(0).asNameExpr();
                String argumentName = argumentExpr.getNameAsString();

                boolean passesParameter = false;
                List<EligibleSubscriptionParameter> possibleParams = eligibleParams.get(operatesOn);
                for (EligibleSubscriptionParameter parameter : possibleParams) {
                    if (parameter.getParameter().getNameAsString().equals(argumentName)) {
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
            } catch (UnsolvedSymbolException ex) {
                // FIXME Fix exception log
            }
        }
    }

    private ResolvedValueDeclaration getResolvedScopeField (final MethodCallExpr methodCall) {
        Optional<Expression> optionalScope = methodCall.getScope();
        if (!optionalScope.isPresent()) {
            return null;
        }

        Expression scopeExpression = optionalScope.get();
        ResolvedValueDeclaration scope = null;

        if (scopeExpression.isNameExpr()) {
            scope = JavaParserFacade.get(getTypeSolver()).solve(scopeExpression).getCorrespondingDeclaration();
        } else if (scopeExpression.isFieldAccessExpr()) {
            scope = scopeExpression.asFieldAccessExpr().resolve();
        }

        if (scope instanceof JavaParserFieldDeclaration) {
            JavaParserFieldDeclaration scopeSymbol = (JavaParserFieldDeclaration) scope;
            if (scopeSymbol.getWrappedNode() != null) {
                FieldDeclaration scopeField = scopeSymbol.getWrappedNode().asFieldDeclaration();
                ResolvedValueDeclaration resScopeField = null;

                for (VariableDeclarator fieldVariable : scopeField.getVariables()) {
                    if (fieldVariable.getNameAsString().equals(scope.getName())) {
                        resScopeField = fieldVariable.resolve();
                    }
                }

                return resScopeField;
            }
        }

        return null;
    }

    /**
     * Holds information on a potential subscription parameter.
     */
    private class EligibleSubscriptionParameter {
        /**
         * The parameter node.
         */
        private Parameter parameter;

        /**
         * The resolved type of the parameter.
         */
        private ResolvedReferenceType resolvedType;

        /**
         * The name of the parameter.
         */
        private String parameterName;

        /**
         * @return The parameter node
         */
        public Parameter getParameter() {
            return parameter;
        }

        /**
         * @param parameter The parameter node
         */
        public void setParameter(final Parameter parameter) {
            this.parameter = parameter;
        }

        /**
         * @return The resolved type of the parameter
         */
        public ResolvedReferenceType getResolvedReferenceType() {
            return resolvedType;
        }

        /**
         * @param resolvedReferenceType The resolved type of the parameter
         */
        public void setResolvedReferenceType(ResolvedReferenceType resolvedReferenceType) {
            this.resolvedType = resolvedReferenceType;
        }

        /**
         * @return The name of the parameter
         */
        public String getParameterName() {
            return parameterName;
        }

        /**
         * @param parameterName The name of the parameter
         */
        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }
    }
}
