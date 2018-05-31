package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

public class AbstractObserver {
    private ClassOrInterfaceDeclaration classDeclaration;
    private ResolvedReferenceTypeDeclaration resolvedTypeDeclaration;
    private ResolvedMethodDeclaration updateMethod;
    private VariableDeclarator observableVariable;

    public AbstractObserver (final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null, null);
    }

    public AbstractObserver (final ClassOrInterfaceDeclaration classDeclaration, final ResolvedReferenceTypeDeclaration resolvedTypeDeclaration) {
        this(classDeclaration, resolvedTypeDeclaration, null);
    }

    public AbstractObserver (final ClassOrInterfaceDeclaration classDeclaration, final ResolvedReferenceTypeDeclaration resolvedTypeDeclaration, final ResolvedMethodDeclaration updateMethod) {
        this.classDeclaration = classDeclaration;
        this.resolvedTypeDeclaration = resolvedTypeDeclaration;
        this.updateMethod = updateMethod;
        this.observableVariable = null;
    }

    public ClassOrInterfaceDeclaration getClassDeclaration() {
        return classDeclaration;
    }

    public boolean isInterface () {
        return classDeclaration.isInterface();
    }

    public boolean isClass () {
        return !classDeclaration.isInterface();
    }

    public ResolvedReferenceTypeDeclaration getResolvedTypeDeclaration() {
        if (resolvedTypeDeclaration == null) {
            // TODO Exception gooien als dit niet lukt
            resolvedTypeDeclaration = classDeclaration.resolve();
        }

        return resolvedTypeDeclaration;
    }

    public ResolvedMethodDeclaration getUpdateMethod() {
        return updateMethod;
    }

    public AbstractObserver setUpdateMethod(final ResolvedMethodDeclaration updateMethod) {
        this.updateMethod = updateMethod;
        return this;
    }

    public VariableDeclarator getObservableVariable () {
        return observableVariable;
    }

    public AbstractObserver setObservableVariable (final VariableDeclarator observableVariable) {
        this.observableVariable = observableVariable;
        return this;
    }
}
