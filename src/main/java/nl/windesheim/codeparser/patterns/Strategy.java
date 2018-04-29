package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.FilePart;

import java.util.ArrayList;

/**
 * Created by caveman on 4/19/18.
 */
public class Strategy implements IDesignPattern {
    private FilePart context;
    private FilePart strategyInterface;
    private ArrayList<FilePart> strategies;

    public FilePart getContext() {
        return context;
    }

    public Strategy setContext(FilePart context) {
        this.context = context;
        return this;
    }

    public FilePart getStrategyInterface() {
        return strategyInterface;
    }

    public Strategy setStrategyInterface(FilePart strategyInterface) {
        this.strategyInterface = strategyInterface;
        return this;
    }

    public ArrayList<FilePart> getStrategies() {
        return strategies;
    }

    public Strategy setStrategies(ArrayList<FilePart> strategies) {
        this.strategies = strategies;
        return this;
    }
}
