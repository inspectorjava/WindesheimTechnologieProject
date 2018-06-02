package nl.windesheim.reporting;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.patterns.ChainOfResponsibility;
import nl.windesheim.codeparser.patterns.CompositePattern;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Singleton;
import nl.windesheim.codeparser.patterns.Strategy;
import nl.windesheim.reporting.builders.ChainOfResponsibilityFoundPatternBuilder;
import nl.windesheim.reporting.builders.CompositeFoundBuilder;
import nl.windesheim.reporting.builders.SingletonFoundPatternBuilder;
import nl.windesheim.reporting.builders.StrategyFoundPatternBuilder;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;

/**
 * Map the result from analyzers and return the correct builder.
 */
public class CodeReportDesignPatternMapper {
    /**
     * Get the correct builder.
     * @param pattern IDesignPattern result
     * @return AbstractFoundPatternBuilder builder of matched result
     */
    public AbstractFoundPatternBuilder getBuilder(final IDesignPattern pattern) {
        // Singletons
        if (pattern instanceof Singleton) {
            return buildSingletonBuilder((Singleton) pattern);
        }

        // Chain of responsibility
        if (pattern instanceof ChainOfResponsibility) {
            return buildChainOfResponsibilityBuilder((ChainOfResponsibility) pattern);
        }

        // Strategy
        if (pattern instanceof Strategy) {
            return buildStrategyBuilder((Strategy) pattern);
        }

        //Composite
        if (pattern instanceof CompositePattern) {
            return buildCompositeBuilder((CompositePattern) pattern);
        }

        return null;
    }

    /**
     * Build the strategy builder.
     * @param pattern strategy pattern
     * @return Strategy builder
     */
    private AbstractFoundPatternBuilder buildStrategyBuilder(final Strategy pattern) {
        ClassOrInterface interfaceName = pattern.getStrategyInterface();
        ClassOrInterface context = pattern.getContext();

        return new StrategyFoundPatternBuilder(context, interfaceName, pattern.getStrategies());
    }

    /**
     * Build the singleton builder.
     * @param pattern singleton pattern
     * @return Singleton builder
     */
    private AbstractFoundPatternBuilder buildSingletonBuilder(final Singleton pattern) {
        return new SingletonFoundPatternBuilder(pattern.getSingletonClass());
    }

    /**
     * Build the ChainOfResponsbilityBuilder.
     * @param pattern the pattern
     * @return ChainOfResponsibilityBuilder
     */
    private AbstractFoundPatternBuilder buildChainOfResponsibilityBuilder(final ChainOfResponsibility pattern) {
        return new ChainOfResponsibilityFoundPatternBuilder(pattern.getCommonParent(), pattern.getChainLinks());
    }

    /**
     * Build the CompositeFoundBuilder.
     * @param pattern the pattern
     * @return ChainOfResponsibilityBuilder
     */
    private AbstractFoundPatternBuilder buildCompositeBuilder(final CompositePattern pattern) {
        return new CompositeFoundBuilder(pattern.getComponent(), pattern.getComposites(), pattern.getLeafs());
    }
}
