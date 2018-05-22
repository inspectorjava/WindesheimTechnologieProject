package nl.windesheim.reporting.builders;

import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasContext;
import nl.windesheim.reporting.decorators.HasFiles;
import nl.windesheim.reporting.decorators.HasInterface;
import nl.windesheim.reporting.decorators.HasStrategies;

import java.util.List;

/**
 * Strategy pattern found builder.
 */
public class StrategyFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * Files list.
     */
    private final List<String> files;

    /**
     * Context of strategy.
     */
    private final String context;

    /**
     * Strategy interface.
     */
    private final String strategyInterface;

    /**
     * Strategies list.
     */
    private final  List<String> strategies;

    /**
     * Set the required parameters for the builder.
     * @param files the files used to create a strategy pattern
     * @param context the context of the strategy pattern
     * @param strategyInterface the interface of the strategy pattern
     * @param strategies the strategies provided inside the strategy pattern
     */
    public StrategyFoundPatternBuilder(
            final List<String> files,
            final String context,
            final String strategyInterface,
            final List<String> strategies
    ) {
        super();
        this.files = files;
        this.context = context;
        this.strategyInterface = strategyInterface;
        this.strategies = strategies;
    }


    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.STRATEGY);

        HasStrategies strategy = new HasStrategies(patternReport);
        strategy.setStrategies(this.strategies);

        HasFiles hasFiles = new HasFiles(strategy);
        hasFiles.setFiles(this.files);


        HasContext hasContext = new HasContext(hasFiles);
        hasContext.setContext(this.context);

        HasInterface hasInterface = new HasInterface(hasContext);
        hasInterface.setInterfaceName(this.strategyInterface);

        return hasInterface;
    }
}
