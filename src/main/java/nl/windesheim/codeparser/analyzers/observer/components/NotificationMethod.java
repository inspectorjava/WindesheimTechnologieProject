package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

/**
 * Encapsulates information on a notification method, as implemented in an abstract observable class.
 */
public class NotificationMethod {
    /**
     * The declaration of the notification method.
     */
    private MethodDeclaration methodDeclaration;

    /**
     * The call to the update method, as performed in the notification method.
     */
    private MethodCallExpr updateMethodCall;

    /**
     * NotificationMethod constructor.
     *
     * @param methodDeclaration The declaration of the notification method
     * @param updateMethodCall  The call to the update method, as performed in the notification method
     */
    public NotificationMethod(final MethodDeclaration methodDeclaration, final MethodCallExpr updateMethodCall) {
        this.methodDeclaration = methodDeclaration;
        this.updateMethodCall = updateMethodCall;
    }

    /**
     * @return The declaration of the notification method
     */
    public MethodDeclaration getMethodDeclaration() {
        return methodDeclaration;
    }

    /**
     * @param methodDeclaration The declaration of the notification method
     * @return this
     */
    public NotificationMethod setMethodDeclaration(final MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
        return this;
    }

    /**
     * @return The call to the update method, as performed in the notification method
     */
    public MethodCallExpr getUpdateMethodCall() {
        return updateMethodCall;
    }

    /**
     * @param updateMethodCall The call to the update method, as performed in the notification method
     * @return this
     */
    public NotificationMethod setUpdateMethodCall(final MethodCallExpr updateMethodCall) {
        this.updateMethodCall = updateMethodCall;
        return this;
    }
}
