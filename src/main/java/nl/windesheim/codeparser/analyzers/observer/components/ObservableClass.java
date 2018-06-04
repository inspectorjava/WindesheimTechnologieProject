package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

/**
 * Encapsulates information on an observable class.
 */
public abstract class ObservableClass extends EligibleObserverComponent {
    /**
     * ObservableClass constructor.
     *
     * @param classDeclaration The class or interface defining the observable
     */
    public ObservableClass(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null);
    }

    /**
     * ObservableClass constructor.
     *
     * @param classDeclaration        The class or interface defining the observable
     * @param resolvedType The type of the observable class
     */
    public ObservableClass(
            final ClassOrInterfaceDeclaration classDeclaration,
            final ResolvedReferenceTypeDeclaration resolvedType
    ) {
        super(classDeclaration, resolvedType);
    }
}
