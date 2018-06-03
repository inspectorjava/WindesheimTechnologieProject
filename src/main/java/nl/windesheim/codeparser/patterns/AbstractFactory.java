package nl.windesheim.codeparser.patterns;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

/**
 * The Abstract Factory pattern class.
 */
public class AbstractFactory implements IDesignPattern {

    /**
     * The interface that indicates the factory.
     */
    private ClassOrInterfaceDeclaration factoryInterface;

    /**
     * Get the factory interface.
     * @return the factory interface.
     */
    public ClassOrInterfaceDeclaration getFactoryInterface() {
        return factoryInterface;
    }

    /**
     * Set the factory interface.
     * @param factoryInterface the factory interface.
     */
    public void setFactoryInterface(final ClassOrInterfaceDeclaration factoryInterface) {
        this.factoryInterface = factoryInterface;
    }
}
