package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

/**
 * Encapsulates information on an subject class.
 */
public abstract class SubjectClass extends EligibleObserverComponent {
    /**
     * SubjectClass constructor.
     *
     * @param classDeclaration The class or interface defining the subject
     */
    public SubjectClass(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null);
    }

    /**
     * SubjectClass constructor.
     *
     * @param classDeclaration        The class or interface defining the subject
     * @param resolvedType The type of the subject class
     */
    public SubjectClass(
            final ClassOrInterfaceDeclaration classDeclaration,
            final ResolvedReferenceTypeDeclaration resolvedType
    ) {
        super(classDeclaration, resolvedType);
    }
}
