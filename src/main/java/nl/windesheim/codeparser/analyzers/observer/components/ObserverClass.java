package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

/**
 * Encapsulates information on an observer class.
 */
public class ObserverClass extends EligibleObserverComponent {
    /**
     * The declaration of the update method.
     */
    private ResolvedMethodDeclaration updateMethod;

    /**
     * ObserverClass constructor.
     *
     * @param classDeclaration The class or interface defining the observer
     */
    public ObserverClass(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null);
    }

    /**
     * ObserverClass constructor.
     *
     * @param classDeclaration The class or interface defining the observer
     * @param resolvedType     The type of the observable class
     */
    public ObserverClass(
            final ClassOrInterfaceDeclaration classDeclaration,
            final ResolvedReferenceTypeDeclaration resolvedType
    ) {
        this(classDeclaration, resolvedType, null);
    }

    /**
     * ObserverClass constructor.
     *
     * @param classDeclaration The class or interface defining the observer
     * @param resolvedType     The type of the observer class
     * @param updateMethod     The declaration of the update method
     */
    public ObserverClass(final ClassOrInterfaceDeclaration classDeclaration,
                            final ResolvedReferenceTypeDeclaration resolvedType,
                            final ResolvedMethodDeclaration updateMethod) {
        super(classDeclaration, resolvedType);
        this.updateMethod = updateMethod;
    }

    /**
     * @return The declaration of the update method associated with the abstract observer
     */
    public ResolvedMethodDeclaration getUpdateMethod() {
        return updateMethod;
    }

    /**
     * @param updateMethod The declaration of the update method associated with the abstract observer
     */
    public void setUpdateMethod(final ResolvedMethodDeclaration updateMethod) {
        this.updateMethod = updateMethod;
    }
}
