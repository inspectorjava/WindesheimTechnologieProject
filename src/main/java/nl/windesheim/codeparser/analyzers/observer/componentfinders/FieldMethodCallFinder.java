package nl.windesheim.codeparser.analyzers.observer.componentfinders;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public class FieldMethodCallFinder extends GenericVisitorAdapter<Boolean, List<MethodDeclaration>> {
    @Override
    public Boolean visit(final MethodCallExpr methodCallExpr, final List<MethodDeclaration> subscribeMethods) {
        Boolean parentHasFound = super.visit(methodCallExpr, subscribeMethods);
        if (parentHasFound != null) {
            return parentHasFound;
        }

        List<ResolvedMethodDeclaration> resSubsMethods = new ArrayList<>();
        for (MethodDeclaration subscribeMethod : subscribeMethods) {
            resSubsMethods.add(subscribeMethod.resolve());
        }

        ResolvedMethodDeclaration resolvedCall = methodCallExpr.resolve();
        for (ResolvedMethodDeclaration resSubsMethod : resSubsMethods) {
            if (resSubsMethod.getQualifiedSignature().equals(resolvedCall.getQualifiedSignature())) {
                return true;
            }
        }

        return null;
    }
}
