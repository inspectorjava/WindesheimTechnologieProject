package nl.windesheim.reporting;

import nl.windesheim.codeparser.patterns.AbstractFactory;
import nl.windesheim.codeparser.patterns.ChainOfResponsibility;
import nl.windesheim.codeparser.patterns.Command;
import nl.windesheim.codeparser.patterns.CompositePattern;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.ObserverPattern;
import nl.windesheim.codeparser.patterns.Singleton;
import nl.windesheim.codeparser.patterns.Strategy;
import nl.windesheim.reporting.builders.AbstractFactoryFoundPatternBuilder;
import nl.windesheim.reporting.builders.ChainOfResponsibilityFoundPatternBuilder;
import nl.windesheim.reporting.builders.CommandFoundPatternBuilder;
import nl.windesheim.reporting.builders.CompositeFoundBuilder;
import nl.windesheim.reporting.builders.ObserverFoundPatternBuilder;
import nl.windesheim.reporting.builders.SingletonFoundPatternBuilder;
import nl.windesheim.reporting.builders.StrategyFoundPatternBuilder;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;

/**
 * Map the result from analyzers and return the correct builder.
 */
public class CodeReportDesignPatternMapper {
    /**
     * Get the correct builder.
     *
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

        // Abstract factory
        if (pattern instanceof AbstractFactory) {
            return buildAbstractFactory((AbstractFactory) pattern);
        }

        // Command
        if (pattern instanceof Command) {
            return buildCommandBuilder((Command) pattern);
        }

        //Composite
        if (pattern instanceof CompositePattern) {
            return buildCompositeBuilder((CompositePattern) pattern);
        }

        //Observer
        if (pattern instanceof ObserverPattern) {
            return buildObserverBuilder((ObserverPattern) pattern);
        }

        return null;
    }

    /**
     * Build the abstract factory pattern builder class.
     * @param pattern the pattern.
     * @return the abstract factory pattern builder.
     */
    private AbstractFoundPatternBuilder buildAbstractFactory(final AbstractFactory pattern) {
        return new AbstractFactoryFoundPatternBuilder(pattern);
    }

    /**
     * Build the strategy builder.
     *
     * @param pattern strategy pattern
     * @return Strategy builder
     */
    private AbstractFoundPatternBuilder buildStrategyBuilder(final Strategy pattern) {
        return new StrategyFoundPatternBuilder(pattern);
    }

    /**
     * Build the singleton builder.
     *
     * @param pattern singleton pattern
     * @return Singleton builder
     */
    private AbstractFoundPatternBuilder buildSingletonBuilder(final Singleton pattern) {
        return new SingletonFoundPatternBuilder(pattern.getSingletonClass());
    }

    /**
     * Build the ChainOfResponsbilityBuilder.
     *
     * @param pattern the pattern
     * @return ChainOfResponsibilityBuilder
     */
    private AbstractFoundPatternBuilder buildChainOfResponsibilityBuilder(final ChainOfResponsibility pattern) {
        return new ChainOfResponsibilityFoundPatternBuilder(pattern.getCommonParent(), pattern.getChainLinks());
    }

    /**
     * Build the CompositeFoundBuilder.
     *
     * @param pattern the pattern
     * @return ChainOfResponsibilityBuilder
     */
    private AbstractFoundPatternBuilder buildCompositeBuilder(final CompositePattern pattern) {
        return new CompositeFoundBuilder(pattern.getComponent(), pattern.getComposites(), pattern.getLeafs());
    }

    /**
     * Build the Command builder.
     *
     * @param pattern Command pattern
     * @return Command builder
     */
    private AbstractFoundPatternBuilder buildCommandBuilder(final Command pattern) {
        return new CommandFoundPatternBuilder(pattern.getCommandParent(), pattern.getCommands(),
                pattern.getReceivers());
    }

    /**
     * Build the ObserverFoundBuilder.
     *
     * @param pattern the pattern
     * @return ChainOfResponsibilityBuilder
     */
    private AbstractFoundPatternBuilder buildObserverBuilder(final ObserverPattern pattern) {
        return new ObserverFoundPatternBuilder(
                pattern.getAbstractObservable(),
                pattern.getConcreteObservables(),
                pattern.getAbstractObserver(),
                pattern.getConcreteObservers()
        );
    }
}
