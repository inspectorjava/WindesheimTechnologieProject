package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates information on a concrete observable class.
 */
public class ConcreteObservable extends ObserverClass {
    /**
     * ConcreteObservable constructor.
     *
     * Never called, just here to silence PMD warnings
     */
    private ConcreteObservable() {
        super(null, null);
    }

    /**
     * ConcreteObservable constructor.
     *
     * @param classDeclaration The class or interface defining the concrete observable
     */
    public ConcreteObservable(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null);
    }

    /**
     * ConcreteObservable constructor.
     *
     * @param classDeclaration The class or interface defining the concrete observable
     * @param resolvedType     The type of the concrete observable class
     */
    public ConcreteObservable(final ClassOrInterfaceDeclaration classDeclaration,
                              final ResolvedReferenceTypeDeclaration resolvedType) {
        super(classDeclaration, resolvedType);
    }

    /**
     * Converts a list of classes or interfaces into ConcreteObservable objects.
     *
     * @param classDeclarations A list of classes or interfaces defining a concrete observable
     * @return A list of ConcreteObservables
     */
    public static List<ConcreteObservable> fromClasses(final List<ClassOrInterfaceDeclaration> classDeclarations) {
        List<ConcreteObservable> cObservables = new ArrayList<>();

        for (ClassOrInterfaceDeclaration classDeclaration : classDeclarations) {
            cObservables.add(new ConcreteObservable(classDeclaration));
        }

        return cObservables;
    }
}
