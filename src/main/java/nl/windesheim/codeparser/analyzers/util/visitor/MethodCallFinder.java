package nl.windesheim.codeparser.analyzers.util.visitor;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;

import java.util.List;

/**
 * Searches for calls to the given method.
 */
public class MethodCallFinder extends GenericVisitorAdapter<Boolean, List<ResolvedMethodDeclaration>> {
    @Override
    public Boolean visit(final MethodCallExpr methodCallExpr, final List<ResolvedMethodDeclaration> resMethodDecls) {
        if (resMethodDecls.isEmpty()) {
            return false;
        }

        Boolean parentHasFound = super.visit(methodCallExpr, resMethodDecls);
        if (parentHasFound != null) {
            return parentHasFound;
        }

        ResolvedMethodDeclaration resolvedCall = methodCallExpr.resolve();
        for (ResolvedMethodDeclaration resSubsMethod : resMethodDecls) {
            if (resSubsMethod.getQualifiedSignature().equals(resolvedCall.getQualifiedSignature())) {
                return true;
            }
        }

        return null;
    }
}
