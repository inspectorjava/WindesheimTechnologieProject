package nl.windesheim.codeparser.analyzers.util.visitor;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

import java.util.Optional;

/**
 * Tries to find a setter for a field.
 */
public class SetterFinder extends GenericVisitorAdapter<Boolean, VariableDeclarator> {

    @Override
    public Boolean visit(final MethodDeclaration method, final VariableDeclarator variable) {
        Boolean parentHasFound = super.visit(method, variable);
        if (parentHasFound != null) {
            return parentHasFound;
        }

        Type fieldType = variable.getType();

        Optional<Parameter> parameter = method.getParameterByType(fieldType.asString());
        if (!parameter.isPresent()) {
            return null;
        }

        Optional<BlockStmt> optionalBody = method.getBody();
        if (!optionalBody.isPresent()) {
            return null;
        }
        BlockStmt body = optionalBody.get();

        for (Statement statement : body.getStatements()) {
            if (isSetter(statement, variable)) {
                return true;
            }
        }
        return null;
    }

    /**
     * Checks of a statement is a setter for a variable.
     *
     * @param statement the statement to check
     * @param variable  the variable to check
     * @return boolean
     */
    private boolean isSetter(final Statement statement, final VariableDeclarator variable) {
        if (!(statement instanceof ExpressionStmt)) {
            return false;
        }

        ExpressionStmt expressionStmt = (ExpressionStmt) statement;

        Expression expression = expressionStmt.getExpression();

        if (!(expression instanceof AssignExpr)) {
            return false;
        }

        Expression target = ((AssignExpr) expression).getTarget();
        if (target instanceof FieldAccessExpr) {
            if (((FieldAccessExpr) target).getScope() instanceof ThisExpr
                    && ((FieldAccessExpr) target).getName().equals(variable.getName())
            ) {
                return true;
            }
        } else if (target instanceof NameExpr
                && ((NameExpr) target).getName().equals(variable.getName())
        ) {
            return true;
        }

        return false;
    }

}
