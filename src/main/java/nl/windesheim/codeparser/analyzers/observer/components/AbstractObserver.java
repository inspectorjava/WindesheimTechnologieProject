package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

/**
 * Encapsulates information on an abstract observer class.
 */
public class AbstractObserver extends ObserverClass  {
    /**
     * AbstractObserver constructor.
     *
     * @param classDeclaration The class or interface which represents the abstract observer
     */
    public AbstractObserver(final ClassOrInterfaceDeclaration classDeclaration) {
        super(classDeclaration);
    }

    /**
     * AbstractObserver constructor.
     *
     * @param classDeclaration The class or interface which represents the abstract observer
     * @param resolvedType     The resolved reference type of the abstract observer class or interface
     */
    public AbstractObserver(final ClassOrInterfaceDeclaration classDeclaration,
                            final ResolvedReferenceTypeDeclaration resolvedType) {
        super(classDeclaration, resolvedType);
    }

    /**
     * AbstractObserver constructor.
     *
     * @param classDeclaration The class or interface which represents the abstract observer
     * @param resolvedType     The resolved reference type of the abstract observer class or interface
     * @param updateMethod     The declaration of the update method
     */
    public AbstractObserver(final ClassOrInterfaceDeclaration classDeclaration,
                            final ResolvedReferenceTypeDeclaration resolvedType,
                            final ResolvedMethodDeclaration updateMethod) {
        super(classDeclaration, resolvedType, updateMethod);
    }
}
