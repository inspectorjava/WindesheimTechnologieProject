package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates information on a concrete subject class.
 */
@SuppressWarnings("PMD.UseUtilityClass")
public class ConcreteSubject extends SubjectClass {
    /**
     * ConcreteSubject constructor.
     *
     * @param classDeclaration The class or interface defining the concrete subject
     */
    public ConcreteSubject(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null);
    }

    /**
     * ConcreteSubject constructor.
     *
     * @param classDeclaration The class or interface defining the concrete subject
     * @param resolvedType     The type of the concrete subject class
     */
    public ConcreteSubject(final ClassOrInterfaceDeclaration classDeclaration,
                           final ResolvedReferenceTypeDeclaration resolvedType) {
        super(classDeclaration, resolvedType);
    }

    /**
     * Converts a list of classes or interfaces into ConcreteSubject objects.
     *
     * @param classDeclarations A list of classes or interfaces defining a concrete subject
     * @return A list of ConcreteSubjects
     */
    public static List<ConcreteSubject> fromClasses(final List<ClassOrInterfaceDeclaration> classDeclarations) {
        List<ConcreteSubject> concreteSubjects = new ArrayList<>();

        for (ClassOrInterfaceDeclaration classDeclaration : classDeclarations) {
            concreteSubjects.add(new ConcreteSubject(classDeclaration));
        }

        return concreteSubjects;
    }
}
