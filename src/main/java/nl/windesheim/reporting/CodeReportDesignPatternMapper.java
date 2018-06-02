package nl.windesheim.reporting;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.patterns.*;
import nl.windesheim.reporting.builders.ChainOfResponsibilityFoundPatternBuilder;
import nl.windesheim.reporting.builders.CommandFoundPatternBuilder;
import nl.windesheim.reporting.builders.SingletonFoundPatternBuilder;
import nl.windesheim.reporting.builders.StrategyFoundPatternBuilder;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;

import java.util.ArrayList;
import java.util.List;

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

        // Command
        if (pattern instanceof Command) {
            return buildCommandBuilder((Command) pattern);
        }

        return null;
    }

    /**
     * Build the strategy builder.
     * @param pattern strategy pattern
     * @return Strategy builder
     */
    private AbstractFoundPatternBuilder buildStrategyBuilder(final Strategy pattern) {
        String interfaceName = pattern.getStrategyInterface().getName();
        String context = pattern.getContext().getName();
        List<String> files = new ArrayList<>();
        List<String> strategies = new ArrayList<>();

        for (ClassOrInterface strategy : pattern.getStrategies()) {
            strategies.add(strategy.getName());
        }

        return new StrategyFoundPatternBuilder(files, context, interfaceName, strategies);
    }

    /**
     * Build the singleton builder.
     * @param pattern singleton pattern
     * @return Singleton builder
     */
    private AbstractFoundPatternBuilder buildSingletonBuilder(final Singleton pattern) {
        ClassOrInterface filePart = pattern.getSingletonClass();
        String fileName = filePart.getFilePart().getFile().getName();
        return new SingletonFoundPatternBuilder(fileName);
    }

    /**
     * Build the ChainOfResponsbilityBuilder.
     * @param pattern the pattern
     * @return ChainOfResponsibilityBuilder
     */
    private AbstractFoundPatternBuilder buildChainOfResponsibilityBuilder(final ChainOfResponsibility pattern) {

        List<String> links = new ArrayList<>();


        for (ClassOrInterface link : pattern.getChainLinks()) {
            links.add(link.getName());
        }

        return new ChainOfResponsibilityFoundPatternBuilder(pattern.getCommonParent().getName(), links);
    }

    /**
     * Build the Command builder.
     * @param pattern Command pattern
     * @return Command builder
     */
    private AbstractFoundPatternBuilder buildCommandBuilder(final Command pattern) {
        String interfaceName = pattern.getCommandParent().getName();

        List<String> files = new ArrayList<>();

        List<String> commands = new ArrayList<>();
        for (ClassOrInterface command : pattern.getCommands()) {
            commands.add(command.getName());
        }

        List<String> receivers = new ArrayList<>();
        for (ClassOrInterface command : pattern.getReceivers()) {
            receivers.add(command.getName());
        }

        return new CommandFoundPatternBuilder(files, interfaceName, commands, receivers);
    }

}
