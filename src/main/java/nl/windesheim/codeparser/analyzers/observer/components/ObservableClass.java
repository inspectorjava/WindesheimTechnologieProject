package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import java.util.List;

public abstract class ObservableClass {
    private ClassOrInterfaceDeclaration classDeclaration;
    private ResolvedReferenceTypeDeclaration resolvedTypeDeclaration;

    public ObservableClass(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null);
    }

    public ObservableClass(final ClassOrInterfaceDeclaration classDeclaration, final ResolvedReferenceTypeDeclaration resolvedTypeDeclaration) {
        this.classDeclaration = classDeclaration;
        this.resolvedTypeDeclaration = resolvedTypeDeclaration;
    }

    public ClassOrInterfaceDeclaration getClassDeclaration() {
        return classDeclaration;
    }

    public ResolvedReferenceTypeDeclaration getResolvedTypeDeclaration () {
        // TODO Gooi een exception op als dit niet lukt
        if (resolvedTypeDeclaration == null) {
            resolvedTypeDeclaration = classDeclaration.resolve();
        }

        return resolvedTypeDeclaration;
    }
}
