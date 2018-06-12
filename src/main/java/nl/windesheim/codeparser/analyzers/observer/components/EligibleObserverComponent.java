package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

/**
 * Encapsulates information on an subject class.
 */
public class EligibleObserverComponent {
    /**
     * The class or interface defining the subject.
     */
    private ClassOrInterfaceDeclaration classDeclaration;

    /**
     * The type of the subject class.
     */
    private ResolvedReferenceTypeDeclaration resolvedType;

    /**
     * EligibleObserverComponent constructor.
     *
     * @param classDeclaration The class or interface defining the subject
     */
    public EligibleObserverComponent(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null);
    }

    /**
     * EligibleObserverComponent constructor.
     *
     * @param classDeclaration        The class or interface defining the subject
     * @param resolvedType The type of the subject class
     */
    public EligibleObserverComponent(final ClassOrInterfaceDeclaration classDeclaration,
                                     final ResolvedReferenceTypeDeclaration resolvedType) {
        this.classDeclaration = classDeclaration;
        this.resolvedType = resolvedType;
    }

    /**
     * @return The class or interface defining the subject
     */
    public ClassOrInterfaceDeclaration getClassDeclaration() {
        return classDeclaration;
    }

    /**
     * @return The type of the subject class
     */
    public ResolvedReferenceTypeDeclaration getResolvedTypeDeclaration() {
        if (resolvedType == null) {
            resolvedType = classDeclaration.resolve();
        }

        return resolvedType;
    }
}
