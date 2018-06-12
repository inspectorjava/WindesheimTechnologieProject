package nl.windesheim.codeparser.analyzers.observer.componentfinders;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserSymbolDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.components.NotificationMethod;
import nl.windesheim.codeparser.analyzers.observer.components.ObserverCollection;
import nl.windesheim.codeparser.analyzers.util.ErrorLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Finds notification methods for a potential AbstractSubject.
 *
 * A notification method adheres to the following criteria:
 * - It is a method which loops over an observer collection
 * - A method is being called for each element in the observer collection
 */
public class NotificationMethodFinder extends SubjectMethodFinder {
    /**
     * NotificationMethodFinder constructor.
     *
     * @param typeSolver   A TypeSolver which can be used by this class
     * @param observerCols A list of detected potential observer collections
     */
    public NotificationMethodFinder(
            final TypeSolver typeSolver,
            final List<ObserverCollection> observerCols
    ) {
        super(typeSolver, observerCols);
    }

    @Override
    public void determine(final MethodDeclaration methodDeclaration) {
        // Check if the method loops over an eligible collection
        List<ForeachStmt> foreachStatements = methodDeclaration.findAll(ForeachStmt.class);

        for (ForeachStmt foreachStatement : foreachStatements) {
            ObserverCollection operatesOn = findEligibleForeachStatement(foreachStatement);

            if (operatesOn == null) {
                continue;
            }

            // Check whether a method is being called somewhere in the body of the foreach statement
            VariableDeclarator foreachVariable = foreachStatement.getVariable().getVariable(0);
            List<MethodCallExpr> methodCalls = findMethodCalls(foreachStatement);

            if (methodCalls.isEmpty()) {
                continue;
            }

            // Check whether the method call operates on the 'current' element of the collection being looped over
            MethodCallExpr updateMethodCall = filterMethodCallOnTarget(methodCalls, foreachVariable);

            if (updateMethodCall != null) {
                NotificationMethod notifyMethod = new NotificationMethod(methodDeclaration, updateMethodCall);
                operatesOn.addNotificationMethod(notifyMethod);
            }
        }
    }

    /**
     * Check whether the method call operates on a variable.
     *
     * @param methodCalls A list of method calls
     * @param variable    The variable the method call should operate on
     * @return A methodcall that operates on the given variable
     */
    private MethodCallExpr filterMethodCallOnTarget(final List<MethodCallExpr> methodCalls,
                                                    final VariableDeclarator variable
    ) {
        MethodCallExpr updateMethodCall = null;
        for (MethodCallExpr methodCall : methodCalls) {
            Optional<Expression> optionalScope = methodCall.getScope();

            try {
                if (optionalScope.isPresent() && optionalScope.get().isNameExpr()) {
                    NameExpr scopeExpression = optionalScope.get().asNameExpr();
                    ResolvedValueDeclaration scope =
                            JavaParserFacade
                                    .get(getTypeSolver())
                                    .solve(scopeExpression)
                                    .getCorrespondingDeclaration();

                    if (!(scope instanceof JavaParserSymbolDeclaration)) {
                        continue;
                    }

                    JavaParserSymbolDeclaration scopeSymbol = (JavaParserSymbolDeclaration) scope;
                    if (!(scopeSymbol.getWrappedNode() instanceof VariableDeclarator)) {
                        continue;
                    }

                    VariableDeclarator scopeVariable = (VariableDeclarator) scopeSymbol.getWrappedNode();
                    if (variable.equals(scopeVariable)) {
                        updateMethodCall = methodCall;
                        break;
                    }
                }
            } catch (UnsolvedSymbolException ex) {
                ErrorLog.getInstance().addError(ex);
            }
        }

        return updateMethodCall;
    }

    /**
     * Finds method calls in the body of a foreach statement.
     *
     * @param foreachStatement The foreach statement to analyze
     * @return A list of method calls found in the body of the foreach statement
     */
    private List<MethodCallExpr> findMethodCalls(final ForeachStmt foreachStatement) {
        List<MethodCallExpr> methodCalls = new ArrayList<>();
        Statement foreachBody = foreachStatement.getBody();

        if (foreachBody.isBlockStmt()) {
            BlockStmt foreachBodyBlock = foreachBody.asBlockStmt();
            methodCalls = foreachBodyBlock.findAll(MethodCallExpr.class);
        } else if (foreachBody.isExpressionStmt()) {
            ExpressionStmt foreachBodyExpr = foreachBody.asExpressionStmt();

            if (foreachBodyExpr.getExpression().isMethodCallExpr()) {
                methodCalls.add(foreachBodyExpr.getExpression().asMethodCallExpr());
            }
        }

        return methodCalls;
    }

    /**
     * Finds the potential observer collection which is being operated on in the given foreach statement.
     *
     * @param foreachStatement The foreach statement being analyzed
     * @return The potential observer collection being operated on in the foreach statement, or null when
     *         there is no such operation being performed
     */
    private ObserverCollection findEligibleForeachStatement(final ForeachStmt foreachStatement) {
        Expression iterableExpr = foreachStatement.getIterable();
        ResolvedValueDeclaration iterableValue = null;

        try {
            if (iterableExpr.isNameExpr()) {
                iterableValue =
                        JavaParserFacade
                                .get(getTypeSolver())
                                .solve(iterableExpr)
                                .getCorrespondingDeclaration();
            } else if (iterableExpr.isFieldAccessExpr()) {
                iterableValue = iterableExpr.asFieldAccessExpr().resolve();
            }
        } catch (UnsolvedSymbolException|UnsupportedOperationException ex) {
            ErrorLog.getInstance().addError(ex);
        }

        // Check if iterableValue refers to a class property
        if (iterableValue instanceof JavaParserFieldDeclaration) {
            JavaParserFieldDeclaration iterableField = (JavaParserFieldDeclaration) iterableValue;
            for (ObserverCollection collection : getObserverCollections()) {
                if (collection.getVariableDeclarator().getNameAsString().equals(iterableField.getName())) {
                    return collection;
                }
            }
        }

        return null;
    }
}
