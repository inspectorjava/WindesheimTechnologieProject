package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

/**
 * Encapsulates information on an observable class.
 */
public class EligibleObserverComponent {
    /**
     * The class or interface defining the observable.
     */
    private ClassOrInterfaceDeclaration classDeclaration;

    /**
     * The type of the observable class.
     */
    private ResolvedReferenceTypeDeclaration resolvedType;

    /**
     * EligibleObserverComponent constructor.
     *
     * @param classDeclaration The class or interface defining the observable
     */
    public EligibleObserverComponent(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null);
    }

    /**
     * EligibleObserverComponent constructor.
     *
     * @param classDeclaration        The class or interface defining the observable
     * @param resolvedType The type of the observable class
     */
    public EligibleObserverComponent(final ClassOrInterfaceDeclaration classDeclaration,
                                     final ResolvedReferenceTypeDeclaration resolvedType) {
        this.classDeclaration = classDeclaration;
        this.resolvedType = resolvedType;
    }

    /**
     * @return The class or interface defining the observable
     */
    public ClassOrInterfaceDeclaration getClassDeclaration() {
        return classDeclaration;
    }

    /**
     * @return The type of the observable class
     */
    public ResolvedReferenceTypeDeclaration getResolvedTypeDeclaration() {
        if (resolvedType == null) {
            resolvedType = classDeclaration.resolve();
        }

        return resolvedType;
    }
}
