package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.FilePart;

import java.util.ArrayList;

/**
 * Created by caveman on 4/19/18.
 */
public class Strategy implements IDesignPattern {
    /**
     * The file part which contains the context class of the strategy pattern.
     */
    private FilePart context;

    /**
     * The file part which contains the strategy interface of the strategy pattern.
     */
    private FilePart strategyInterface;

    /**
     * A list of file parts of strategies which can be used in this strategy patterns.
     */
    private ArrayList<FilePart> strategies;

    /**
     * @return the context of the strategy pattern
     */
    public FilePart getContext() {
        return context;
    }

    /**
     * @param context the context of the strategy pattern
     * @return this
     */
    public Strategy setContext(final FilePart context) {
        this.context = context;
        return this;
    }

    /**
     * @return the strategy interface of the strategy pattern
     */
    public FilePart getStrategyInterface() {
        return strategyInterface;
    }

    /**
     * @param strategyInterface the strategy interface of the strategy pattern
     * @return this
     */
    public Strategy setStrategyInterface(final FilePart strategyInterface) {
        this.strategyInterface = strategyInterface;
        return this;
    }

    /**
     * @return a list of strategies which can be used in this strategy pattern
     */
    public ArrayList<FilePart> getStrategies() {
        return strategies;
    }

    /**
     * @param strategies a list of strategies which can be used in this strategy pattern
     * @return this
     */
    public Strategy setStrategies(final ArrayList<FilePart> strategies) {
        this.strategies = strategies;
        return this;
    }
}
