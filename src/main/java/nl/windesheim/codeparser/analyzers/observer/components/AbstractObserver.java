package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

/**
 * Encapsulates information on an abstract observer class.
 */
public class AbstractObserver {
    /**
     * The class or interface which represents the abstract observer.
     */
    private ClassOrInterfaceDeclaration classDeclaration;

    /**
     * The resolved reference type of the abstract observer class or interface.
     */
    private ResolvedReferenceTypeDeclaration resolvedType;

    /**
     * The declaration of the update method.
     */
    private ResolvedMethodDeclaration updateMethod;

    /**
     * AbstractObserver constructor.
     *
     * @param classDeclaration The class or interface which represents the abstract observer
     */
    public AbstractObserver(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null, null);
    }

    /**
     * AbstractObserver constructor.
     *
     * @param classDeclaration        The class or interface which represents the abstract observer
     * @param resolvedType The resolved reference type of the abstract observer class or interface
     */
    public AbstractObserver(final ClassOrInterfaceDeclaration classDeclaration,
                            final ResolvedReferenceTypeDeclaration resolvedType) {
        this(classDeclaration, resolvedType, null);
    }

    /**
     * AbstractObserver constructor.
     *
     * @param classDeclaration        The class or interface which represents the abstract observer
     * @param resolvedType The resolved reference type of the abstract observer class or interface
     * @param updateMethod            The declaration of the update method
     */
    public AbstractObserver(final ClassOrInterfaceDeclaration classDeclaration,
                            final ResolvedReferenceTypeDeclaration resolvedType,
                            final ResolvedMethodDeclaration updateMethod) {
        this.classDeclaration = classDeclaration;
        this.resolvedType = resolvedType;
        this.updateMethod = updateMethod;
    }

    /**
     * @return The class or interface which represents the abstract observer
     */
    public ClassOrInterfaceDeclaration getClassDeclaration() {
        return classDeclaration;
    }

    /**
     * @return The resolved reference type of the abstract observer class or interface
     */
    public ResolvedReferenceTypeDeclaration getResolvedTypeDeclaration() {
        if (resolvedType == null) {
            // TODO Exception gooien als dit niet lukt
            resolvedType = classDeclaration.resolve();
        }

        return resolvedType;
    }

    /**
     * @return The declaration of the update method associated with the abstract observer
     */
    public ResolvedMethodDeclaration getUpdateMethod() {
        return updateMethod;
    }

    /**
     * @param updateMethod The declaration of the update method associated with the abstract observer
     * @return this
     */
    public AbstractObserver setUpdateMethod(final ResolvedMethodDeclaration updateMethod) {
        this.updateMethod = updateMethod;
        return this;
    }
}
