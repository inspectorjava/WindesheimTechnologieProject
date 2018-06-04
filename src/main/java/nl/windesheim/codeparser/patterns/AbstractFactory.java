package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;

import java.util.List;

/**
 * The Abstract Factory pattern class.
 */
@SuppressWarnings("PMD.DataClass")
public class AbstractFactory implements IDesignPattern {

    /**
     * The interface that indicates the factory.
     */
    private ClassOrInterface factoryInterface;

    /**
     * List of all the implementations for this factory.
     */
    private List<ClassOrInterface> implementations;

    /**
     * Get the factory interface.
     * @return the factory interface.
     */
    public ClassOrInterface getFactoryInterface() {
        return factoryInterface;
    }

    /**
     * Get the implementations.
     * @return The implementations.
     */
    public List<ClassOrInterface> getImplementations() {
        return implementations;
    }

    /**
     * Set the factory interface.
     * @param factoryInterface the factory interface.
     */
    public void setFactoryInterface(final ClassOrInterface factoryInterface) {
        this.factoryInterface = factoryInterface;
    }

    /**
     * Set the implementations of this factory.
     * @param implementations The implementations to set.
     */
    public void setImplementations(final List<ClassOrInterface> implementations) {
        this.implementations = implementations;
    }
}
