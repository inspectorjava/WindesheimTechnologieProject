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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotificationMethodFinder extends ObservableMethodFinder {
    public NotificationMethodFinder (final TypeSolver typeSolver, List<ObserverCollection> observerCollections) {
        super(typeSolver, observerCollections);
    }

    @Override
    public void determine (final MethodDeclaration methodDeclaration) {
        // A notify method is a method that loops over an eligible collection, and calls a method
        // on the elements in the collection
        // The method should be public (or protected/package access?)

        // Check if the method loops over an eligible collection
        // TODO Implement check on for- and while-loops
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
            MethodCallExpr updateMethodCall = null;
            for (MethodCallExpr methodCall : methodCalls) {
                Optional<Expression> optionalMethodScope = methodCall.getScope();

                try {
                    if (optionalMethodScope.isPresent() && optionalMethodScope.get().isNameExpr()) {
                        NameExpr scopeExpression = optionalMethodScope.get().asNameExpr();
                        ResolvedValueDeclaration scope = JavaParserFacade.get(typeSolver).solve(scopeExpression).getCorrespondingDeclaration();

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
                NotificationMethod notificationMethod = new NotificationMethod(methodDeclaration, updateMethodCall);
                operatesOn.addNotificationMethod(notificationMethod);
            }
        }
    }

    private ObserverCollection findEligibleForeachStatement (final ForeachStmt foreachStatement) {
        Expression iterableExpression = foreachStatement.getIterable();
        ResolvedValueDeclaration iterableValue = null;

        try {
            if (iterableExpression.isNameExpr()) {
                iterableValue = JavaParserFacade.get(typeSolver).solve(iterableExpression).getCorrespondingDeclaration();
            } else if (iterableExpression.isFieldAccessExpr()) {
                iterableValue = iterableExpression.asFieldAccessExpr().resolve();
            }
        } catch (UnsolvedSymbolException ex) {
            // FIXME Fix exception log
        }

        // Check if iterableValue refers to a class property
        if (iterableValue instanceof JavaParserFieldDeclaration) {
            JavaParserFieldDeclaration iterableField = (JavaParserFieldDeclaration) iterableValue;
            for (ObserverCollection collection : observerCollections) {
                if (collection.getVariableDeclarator().getNameAsString().equals(iterableField.getName())) {
                    return collection;
                }
            }
        }

        return null;
    }
}
