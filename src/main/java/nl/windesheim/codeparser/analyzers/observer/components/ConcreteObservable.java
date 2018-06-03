package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class ConcreteObservable extends ObservableClass {
    public ConcreteObservable(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null);
    }

    public ConcreteObservable(final ClassOrInterfaceDeclaration classDeclaration, final ResolvedReferenceTypeDeclaration resolvedTypeDeclaration) {
        super(classDeclaration, resolvedTypeDeclaration);
    }

    public static List<ConcreteObservable> fromClasses (List<ClassOrInterfaceDeclaration> classDeclarations) {
        List<ConcreteObservable> concreteObservables = new ArrayList<>();

        for (ClassOrInterfaceDeclaration classDeclaration : classDeclarations) {
            concreteObservables.add(new ConcreteObservable(classDeclaration));
        }

        return concreteObservables;
    }
}
