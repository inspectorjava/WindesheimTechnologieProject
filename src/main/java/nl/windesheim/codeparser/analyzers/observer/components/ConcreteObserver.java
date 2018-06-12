package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates information on a concrete observer.
 */
@SuppressWarnings("PMD.UseUtilityClass")
public class ConcreteObserver extends ObserverClass {
    /**
     * ConcreteObserver constructor.
     *
     * @param classDeclaration The class or interface which represents the concrete observer
     */
    public ConcreteObserver(final ClassOrInterfaceDeclaration classDeclaration) {
        super(classDeclaration);
    }

    /**
     * ConcreteObserver constructor.
     *
     * @param classDeclaration        The class or interface which represents the concrete observer
     * @param resolvedType The resolved reference type of the concrete observer class or interface
     * @param updateMethod            The declaration of the update method
     */
    public ConcreteObserver(final ClassOrInterfaceDeclaration classDeclaration,
                            final ResolvedReferenceTypeDeclaration resolvedType,
                            final ResolvedMethodDeclaration updateMethod) {
        super(classDeclaration, resolvedType, updateMethod);
    }

    /**
     * Converts a list of classes or interfaces into ConcreteObserver objects.
     *
     * @param classDeclarations A list of classes or interfaces defining a concrete observer
     * @return A list of ConcreteObservers
     */
    public static List<ConcreteObserver> fromClasses(final List<ClassOrInterfaceDeclaration> classDeclarations) {
        List<ConcreteObserver> concreteObservers = new ArrayList<>();

        for (ClassOrInterfaceDeclaration classDeclaration : classDeclarations) {
            concreteObservers.add(new ConcreteObserver(classDeclaration));
        }

        return concreteObservers;
    }
}
