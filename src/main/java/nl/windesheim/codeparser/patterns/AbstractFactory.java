package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;

import java.util.List;

public class AbstractFactory implements IDesignPattern {

    /**
     * List of all the common interfaces.
     */
    private List<ClassOrInterface> commonInterfaces;

    /**
     * The interface that indicates the factory
     */
    private ClassOrInterface factoryInterface;

    /**
     * Get the common interface.
     * @return the common interface.
     */
    public List<ClassOrInterface> getCommonInterfaces() {
        return commonInterfaces;
    }

    /**
     * Set the common interface.
     * @param commonInterfaces the common interface.
     */
    public void setCommonInterfaces(List<ClassOrInterface> commonInterfaces) {
        this.commonInterfaces = commonInterfaces;
    }

    /**
     * Get the factory interface.
     * @return the factory interface.
     */
    public ClassOrInterface getFactoryInterface() {
        return factoryInterface;
    }

    /**
     * Set the factory interface.
     * @param factoryInterface the factory interface.
     */
    public void setFactoryInterface(ClassOrInterface factoryInterface) {
        this.factoryInterface = factoryInterface;
    }
}
