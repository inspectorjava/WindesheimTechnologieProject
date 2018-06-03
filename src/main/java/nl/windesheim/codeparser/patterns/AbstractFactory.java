package nl.windesheim.codeparser.patterns;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.Collection;
import java.util.List;

/**
 * The Abstract Factory pattern class.
 */
@SuppressWarnings("PMD.DataClass")
public class AbstractFactory implements IDesignPattern {

    /**
     * The interface that indicates the factory.
     */
    private ClassOrInterfaceDeclaration factoryInterface;

    /**
     * List of all the implementations for this factory.
     */
    private List<ClassOrInterfaceDeclaration> implementations;

    /**
     * Get the factory interface.
     * @return the factory interface.
     */
    public ClassOrInterfaceDeclaration getFactoryInterface() {
        return factoryInterface;
    }

    /**
     * Get the implementations.
     * @return The implementations.
     */
    public List<ClassOrInterfaceDeclaration> getImplementations() {
        return implementations;
    }

    /**
     * Set the factory interface.
     * @param factoryInterface the factory interface.
     */
    public void setFactoryInterface(final ClassOrInterfaceDeclaration factoryInterface) {
        this.factoryInterface = factoryInterface;
    }

    /**
     * Set the implementations of this factory.
     * @param implementations The implementations to set.
     */
    public void setImplementations(final Collection<? extends ClassOrInterfaceDeclaration> implementations) {
        this.implementations = (List<ClassOrInterfaceDeclaration>) implementations;
    }
}
