package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class ConcreteObserver {
    private ClassOrInterfaceDeclaration classDeclaration;
    private ResolvedReferenceTypeDeclaration resolvedTypeDeclaration;
    private MethodDeclaration updateMethod;
    private FieldDeclaration observableField;

    public ConcreteObserver (final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null, null);
    }

    public ConcreteObserver (final ClassOrInterfaceDeclaration classDeclaration, final ResolvedReferenceTypeDeclaration resolvedTypeDeclaration) {
        this(classDeclaration, resolvedTypeDeclaration, null);
    }

    public ConcreteObserver (final ClassOrInterfaceDeclaration classDeclaration, final ResolvedReferenceTypeDeclaration resolvedTypeDeclaration, final MethodDeclaration updateMethod) {
        this.classDeclaration = classDeclaration;
        this.resolvedTypeDeclaration = resolvedTypeDeclaration;
        this.updateMethod = updateMethod;
    }

    public ClassOrInterfaceDeclaration getClassDeclaration() {
        return classDeclaration;
    }

    public ResolvedReferenceTypeDeclaration getResolvedTypeDeclaration() {
        if (resolvedTypeDeclaration == null) {
            // TODO Exception gooien als dit niet lukt
            resolvedTypeDeclaration = classDeclaration.resolve();
        }

        return resolvedTypeDeclaration;
    }

    public MethodDeclaration getUpdateMethod() {
        return updateMethod;
    }

    public ConcreteObserver setUpdateMethod(MethodDeclaration updateMethod) {
        this.updateMethod = updateMethod;
        return this;
    }

    public static List<ConcreteObserver> fromClasses (List<ClassOrInterfaceDeclaration> classDeclarations) {
        List<ConcreteObserver> concreteObservers = new ArrayList<>();

        for (ClassOrInterfaceDeclaration classDeclaration : classDeclarations) {
            concreteObservers.add(new ConcreteObserver(classDeclaration));
        }

        return concreteObservers;
    }
}
