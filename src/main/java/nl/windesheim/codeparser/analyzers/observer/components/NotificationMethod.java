package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.List;

public class NotificationMethod {
    private MethodDeclaration methodDeclaration;
    private MethodCallExpr methodCall;

    public NotificationMethod () {
        this(null, null);
    }

    public NotificationMethod (final MethodDeclaration methodDeclaration, final MethodCallExpr methodCall) {
        this.methodDeclaration = methodDeclaration;
        this.methodCall = methodCall;
    }

    public MethodDeclaration getMethodDeclaration() {
        return methodDeclaration;
    }

    public void setMethodDeclaration (final MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
    }

    public MethodCallExpr getMethodCall() {
        return methodCall;
    }

    public void setMethodCall (final MethodCallExpr methodCall) {
        this.methodCall = methodCall;
    }

    public boolean isValid () {
        return methodDeclaration != null && methodCall != null;
    }
}
