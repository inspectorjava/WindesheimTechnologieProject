package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates information on a concrete observer.
 */
public class ConcreteObserver {
    /**
     * The class or interface which represents the concrete observer.
     */
    private ClassOrInterfaceDeclaration classDeclaration;

    /**
     * The resolved reference type of the concrete observer class or interface.
     */
    private ResolvedReferenceTypeDeclaration resolvedType;

    /**
     * The declaration of the update method.
     */
    private MethodDeclaration updateMethod;

    /**
     * ConcreteObserver constructor.
     *
     * @param classDeclaration The class or interface which represents the concrete observer
     */
    public ConcreteObserver(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null, null);
    }

    /**
     * ConcreteObserver constructor.
     *
     * @param classDeclaration        The class or interface which represents the concrete observer
     * @param resolvedType The resolved reference type of the concrete observer class or interface
     */
    public ConcreteObserver(final ClassOrInterfaceDeclaration classDeclaration,
                            final ResolvedReferenceTypeDeclaration resolvedType) {
        this(classDeclaration, resolvedType, null);
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
                            final MethodDeclaration updateMethod) {
        this.classDeclaration = classDeclaration;
        this.resolvedType = resolvedType;
        this.updateMethod = updateMethod;
    }

    /**
     * @return The class or interface which represents the concrete observer
     */
    public ClassOrInterfaceDeclaration getClassDeclaration() {
        return classDeclaration;
    }

    /**
     * @return The resolved reference type of the concrete observer class or interface
     */
    public ResolvedReferenceTypeDeclaration getResolvedTypeDeclaration() {
        if (resolvedType == null) {
            // TODO Exception gooien als dit niet lukt
            resolvedType = classDeclaration.resolve();
        }

        return resolvedType;
    }

    /**
     * @return The declaration of the update method
     */
    public MethodDeclaration getUpdateMethod() {
        return updateMethod;
    }

    /**
     * @param updateMethod The declaration of the update method
     * @return this
     */
    public ConcreteObserver setUpdateMethod(final MethodDeclaration updateMethod) {
        this.updateMethod = updateMethod;
        return this;
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
