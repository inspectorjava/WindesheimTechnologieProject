package nl.windesheim.codeparser.analyzers.strategy;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

/**
 * A potential strategy context.
 */
public class EligibleStrategyContext {

    /**
     * The attribute which holds the reference to the strategy.
     */
    private VariableDeclarator interfaceAttr;

    /**
     * The strategy interface which is referenced by the attribute.
     */
    private ClassOrInterfaceDeclaration strategyClass;

    /**
     * True if there is a setter for the attribute.
     */
    private boolean hasSetter;

    /**
     * True if the interface has declared methods.
     */
    private boolean hasMethods;

    /**
     * @return the attribute which holds the reference to the strategy
     */
    public VariableDeclarator getInterfaceAttr() {
        return interfaceAttr;
    }

    /**
     * @param interfaceAttr the attribute which holds the reference to the strategy
     * @return this
     */
    public EligibleStrategyContext setInterfaceAttr(
            final VariableDeclarator interfaceAttr
    ) {
        this.interfaceAttr = interfaceAttr;
        return this;
    }

    /**
     * @return the strategy interface which is referenced by the attribute
     */
    public ClassOrInterfaceDeclaration getStrategyClass() {
        return strategyClass;
    }

    /**
     * @param strategyClass the strategy interface which is referenced by the attribute
     * @return this
     */
    public EligibleStrategyContext setStrategyClass(
            final ClassOrInterfaceDeclaration strategyClass
    ) {
        this.strategyClass = strategyClass;
        return this;
    }

    /**
     * @return true/false
     */
    public boolean isHasSetter() {
        return hasSetter;
    }

    /**
     * @param hasSetter true/false
     * @return this
     */
    public EligibleStrategyContext setHasSetter(final boolean hasSetter) {
        this.hasSetter = hasSetter;
        return this;
    }

    /**
     * @return true/false
     */
    public boolean isHasMethods() {
        return hasMethods;
    }

    /**
     * @param hasMethods true/false
     * @return this
     */
    public EligibleStrategyContext setHasMethods(final boolean hasMethods) {
        this.hasMethods = hasMethods;
        return this;
    }
}
