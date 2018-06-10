package nl.windesheim.reporting;

import nl.windesheim.codeparser.analyzers.util.ErrorLog;
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
import nl.windesheim.reporting.builders.ObserverFoundPatternBuilder;
import nl.windesheim.reporting.builders.SingletonFoundPatternBuilder;
import nl.windesheim.reporting.builders.StrategyFoundPatternBuilder;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Map the result from analyzers and return the correct builder.
 */
public class CodeReportDesignPatternMapper {

    /**
     * A map of all builders for a design pattern.
     */
    private final Map<Class<? extends IDesignPattern>, Class<? extends AbstractFoundPatternBuilder>>
            builderMap = new HashMap<>();

    /**
     *
     */
    public CodeReportDesignPatternMapper() {
        builderMap.put(Singleton.class, SingletonFoundPatternBuilder.class);
        builderMap.put(ChainOfResponsibility.class, ChainOfResponsibilityFoundPatternBuilder.class);
        builderMap.put(Strategy.class, StrategyFoundPatternBuilder.class);
        builderMap.put(AbstractFactory.class, AbstractFactoryFoundPatternBuilder.class);
        builderMap.put(Command.class, CommandFoundPatternBuilder.class);
        builderMap.put(CompositePattern.class, CommandFoundPatternBuilder.class);
        builderMap.put(ObserverPattern.class, ObserverFoundPatternBuilder.class);
    }

    /**
     * Get the correct builder.
     *
     * @param pattern IDesignPattern result
     * @return AbstractFoundPatternBuilder builder of matched result
     */
    public AbstractFoundPatternBuilder getBuilder(final IDesignPattern pattern) {
        if (!builderMap.containsKey(pattern.getClass())) {
            return null;
        }

        try {
            Class<? extends AbstractFoundPatternBuilder> builderClass = builderMap.get(pattern.getClass());

            Constructor<? extends AbstractFoundPatternBuilder> constructor
                    = builderClass.getConstructor(pattern.getClass());

            return constructor.newInstance(pattern);
        } catch (InstantiationException
                | NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException exception
        ) {
            ErrorLog.getInstance().addError(exception);
            return null;
        }

    }
}
