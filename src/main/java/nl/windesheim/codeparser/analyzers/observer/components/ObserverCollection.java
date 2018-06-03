package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

import java.util.ArrayList;
import java.util.List;

public class ObserverCollection {
    private VariableDeclarator variableDeclarator;
    private ResolvedReferenceType fieldType;
    private ResolvedReferenceType parameterType;
    private List<MethodDeclaration> attachMethods;
    private List<MethodDeclaration> detachMethods;
    private List<NotificationMethod> notificationMethods;

    public ObserverCollection(VariableDeclarator variableDeclarator, ResolvedReferenceType fieldType, ResolvedReferenceType parameterType) {
        this.variableDeclarator = variableDeclarator;
        this.fieldType = fieldType;
        this.parameterType = parameterType;

        attachMethods = new ArrayList<>();
        detachMethods = new ArrayList<>();
        notificationMethods = new ArrayList<>();
    }

    public VariableDeclarator getVariableDeclarator() {
        return variableDeclarator;
    }

    public ResolvedReferenceType getFieldType () {
        return fieldType;
    }

    public ResolvedReferenceType getParameterType() {
        return parameterType;
    }

    public boolean hasAttachMethods () {
        return attachMethods.size() > 0;
    }

    public List<MethodDeclaration> getAttachMethods() {
        return attachMethods;
    }

    public ObserverCollection addAttachMethod(final MethodDeclaration attach) {
        this.attachMethods.add(attach);
        return this;
    }

    public boolean hasDetachMethods () {
        return detachMethods.size() > 0;
    }

    public List<MethodDeclaration> getDetachMethods() {
        return detachMethods;
    }

    public ObserverCollection addDetachMethod(final MethodDeclaration detach) {
        this.detachMethods.add(detach);
        return this;
    }

    public boolean hasNotificationMethods () {
        return notificationMethods.size() > 0;
    }

    public List<NotificationMethod> getNotificationMethods() {
        return notificationMethods;
    }

    public ObserverCollection addNotificationMethod(final NotificationMethod notify) {
        this.notificationMethods.add(notify);
        return this;
    }

    public boolean isObserverCollection () {
        return hasAttachMethods()
               && hasNotificationMethods();
    }
}
