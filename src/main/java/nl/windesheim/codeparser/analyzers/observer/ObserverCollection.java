package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import javassist.compiler.ast.MethodDecl;

import java.util.ArrayList;
import java.util.List;

public class ObserverCollection {
    private VariableDeclarator variableDeclarator;
    private ResolvedReferenceType fieldType;
    private ResolvedReferenceType parameterType;
    private List<MethodDeclaration> attachMethods;
    private List<MethodDeclaration> detachMethods;
    private List<MethodDeclaration> notifyMethods;

    public ObserverCollection(VariableDeclarator variableDeclarator, ResolvedReferenceType fieldType, ResolvedReferenceType parameterType) {
        this.variableDeclarator = variableDeclarator;
        this.fieldType = fieldType;
        this.parameterType = parameterType;

        attachMethods = new ArrayList<>();
        detachMethods = new ArrayList<>();
        notifyMethods = new ArrayList<>();
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

    public void addAttachMethod(final MethodDeclaration attach) {
        this.attachMethods.add(attach);
    }

    public boolean hasDetachMethods () {
        return detachMethods.size() > 0;
    }

    public List<MethodDeclaration> getDetachMethods() {
        return detachMethods;
    }

    public void addDetachMethod(final MethodDeclaration detach) {
        this.detachMethods.add(detach);
    }

    public boolean hasNotifyMethods () {
        return notifyMethods.size() > 0;
    }

    public List<MethodDeclaration> getNotifyMethods() {
        return notifyMethods;
    }

    public void addNotifyMethod(final MethodDeclaration notify) {
        this.notifyMethods.add(notify);
    }

    public boolean isObserverCollection () {
        return hasAttachMethods()
               && hasDetachMethods()
               && hasNotifyMethods();
    }
}
