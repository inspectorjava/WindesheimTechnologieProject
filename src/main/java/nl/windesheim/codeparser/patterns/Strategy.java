package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;

import java.util.ArrayList;

/**
 * Created by caveman on 4/19/18.
 */
public class Strategy implements IDesignPattern {
    /**
     * The file part which contains the context class of the strategy pattern.
     */
    private ClassOrInterface context;

    /**
     * The file part which contains the strategy interface of the strategy pattern.
     */
    private ClassOrInterface strategyInterface;

    /**
     * A list of file parts of strategies which can be used in this strategy patterns.
     */
    private ArrayList<ClassOrInterface> strategies;

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
    public ArrayList<ClassOrInterface> getStrategies() {
        return strategies;
    }

    /**
     * @param strategies a list of strategies which can be used in this strategy pattern
     * @return this
     */
    public Strategy setStrategies(final ArrayList<ClassOrInterface> strategies) {
        this.strategies = strategies;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Strategy pattern\n");

        stringBuilder.append("\tContext: \n\t\t");
            stringBuilder.append(this.context);
        stringBuilder.append("\n");

        stringBuilder.append("\tStrategy interface: \n\t\t");
            stringBuilder.append(this.strategyInterface);
        stringBuilder.append("\n");

        stringBuilder.append("\tStrategies: \n");
            for (ClassOrInterface strategy : this.strategies) {
                stringBuilder.append("\t\t- ");
                stringBuilder.append(strategy);
                stringBuilder.append("\n");
            }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }
}
