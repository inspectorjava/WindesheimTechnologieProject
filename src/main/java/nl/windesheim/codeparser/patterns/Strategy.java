package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;

import java.util.List;

/**
 * Created by caveman on 4/19/18.
 */
public class Strategy implements IDesignPattern {

    /**
     * The file part which contains the context class of the strategy pattern.
     */
    private ClassOrInterface context;

    /**
     * Is true if the context has a setter for the strategy interface.
     */
    private boolean contextHasSetter;

    /**
     * Is true of the context has a function which calls a function in the strategy interface.
     */
    private boolean contextHasCaller;

    /**
     * The file part which contains the strategy interface of the strategy pattern.
     */
    private ClassOrInterface strategyInterface;

    /**
     * Is true of the strategy interface has declared methods.
     */
    private boolean strategyMethods;

    /**
     * A list of file parts of strategies which can be used in this strategy patterns.
     */
    private List<ClassOrInterface> strategies;

    /**
     * @return the context of the strategy pattern
     */
    public ClassOrInterface getContext() {
        return context;
    }

    /**
     * @param context the context of the strategy pattern
     * @return this
     */
    public Strategy setContext(final ClassOrInterface context) {
        this.context = context;
        return this;
    }

    /**
     * @return the strategy interface of the strategy pattern
     */
    public ClassOrInterface getStrategyInterface() {
        return strategyInterface;
    }

    /**
     * @param strategyInterface the strategy interface of the strategy pattern
     * @return this
     */
    public Strategy setStrategyInterface(final ClassOrInterface strategyInterface) {
        this.strategyInterface = strategyInterface;
        return this;
    }

    /**
     * @return a list of strategies which can be used in this strategy pattern
     */
    public List<ClassOrInterface> getStrategies() {
        return strategies;
    }

    /**
     * @param strategies a list of strategies which can be used in this strategy pattern
     * @return this
     */
    public Strategy setStrategies(final List<ClassOrInterface> strategies) {
        this.strategies = strategies;
        return this;
    }

    /**
     * @return true/false
     */
    public boolean isContextHasSetter() {
        return contextHasSetter;
    }

    /**
     * @param contextHasSetter true/false
     * @return this
     */
    public Strategy setContextHasSetter(final boolean contextHasSetter) {
        this.contextHasSetter = contextHasSetter;
        return this;
    }

    /**
     * @return true/false
     */
    public boolean isContextHasCaller() {
        return contextHasCaller;
    }

    /**
     * @param contextHasCaller true/false
     * @return this
     */
    public Strategy setContextHasCaller(final boolean contextHasCaller) {
        this.contextHasCaller = contextHasCaller;
        return this;
    }

    /**
     * @return true/false
     */
    public boolean isStrategyMethods() {
        return strategyMethods;
    }

    /**
     * @param strategyMethods true/false
     * @return this
     */
    public Strategy setStrategyMethods(final boolean strategyMethods) {
        this.strategyMethods = strategyMethods;
        return this;
    }
}
