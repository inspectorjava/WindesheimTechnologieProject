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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Finds notification methods for a potential AbstractObservable.
 *
 * A notification method adheres to the following criteria:
 * - It is a method which loops over an observer collection
 * - A method is being called for each element in the observer collection
 */
public class NotificationMethodFinder extends ObservableMethodFinder {
    /**
     * NotificationMethodFinder constructor.
     *
     * @param typeSolver          A TypeSolver which can be used by this class
     * @param observerCols A list of detected potential observer collections
     */
    public NotificationMethodFinder(final TypeSolver typeSolver, final List<ObserverCollection> observerCols) {
        super(typeSolver, observerCols);
    }

    @Override
    public void determine(final MethodDeclaration methodDeclaration) {
        // Check if the method loops over an eligible collection
        // TODO Implement check on for- and while-loops
        List<ForeachStmt> foreachStatements = methodDeclaration.findAll(ForeachStmt.class);

        for (ForeachStmt foreachStatement : foreachStatements) {
            ObserverCollection operatesOn = findEligibleForeachStatement(foreachStatement);

            if (operatesOn == null) {
                continue;
            }

            // Check whether a method is being called somewhere in the body of the foreach statement
            VariableDeclarator foreachVariable = foreachStatement.getVariable().getVariable(0);

            Statement foreachBody = foreachStatement.getBody();
            List<MethodCallExpr> methodCalls = new ArrayList<>();
            if (foreachBody.isBlockStmt()) {
                BlockStmt foreachBodyBlock = foreachBody.asBlockStmt();
                methodCalls = foreachBodyBlock.findAll(MethodCallExpr.class);
            } else if (foreachBody.isExpressionStmt()) {
                ExpressionStmt foreachBodyExpr = foreachBody.asExpressionStmt();

                if (foreachBodyExpr.getExpression().isMethodCallExpr()) {
                    methodCalls.add(foreachBodyExpr.getExpression().asMethodCallExpr());
                }
            }

            if (methodCalls.isEmpty()) {
                continue;
            }

            // Check whether the method call operates on the 'current' element of the collection being looped over
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
                        if (foreachVariable.equals(scopeVariable)) {
                            updateMethodCall = methodCall;
                            break;
                        }
                    }
                } catch (UnsolvedSymbolException ex) {
                    // FIXME Fix exception log
                }
            }

            if (updateMethodCall != null) {
                NotificationMethod notifyMethod = new NotificationMethod(methodDeclaration, updateMethodCall);
                operatesOn.addNotificationMethod(notifyMethod);
            }
        }
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
        } catch (UnsolvedSymbolException ex) {
            // FIXME Fix exception log
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
