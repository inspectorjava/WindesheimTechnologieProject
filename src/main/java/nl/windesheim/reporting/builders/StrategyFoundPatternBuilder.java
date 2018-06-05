package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasContext;
import nl.windesheim.reporting.decorators.HasInterface;
import nl.windesheim.reporting.decorators.HasStrategies;

import java.util.List;

/**
 * Strategy pattern found builder.
 */
public class StrategyFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * Context of strategy.
     */
    private final ClassOrInterface context;

    /**
     * Strategy interface.
     */
    private final ClassOrInterface strategyInterface;

    /**
     * Strategies list.
     */
    private final  List<ClassOrInterface> strategies;

    /**
     * Set the required parameters for the builder.
     * @param context the context of the strategy pattern
     * @param strategyInterface the interface of the strategy pattern
     * @param strategies the strategies provided inside the strategy pattern
     */
    public StrategyFoundPatternBuilder(
            final ClassOrInterface context,
            final ClassOrInterface strategyInterface,
            final List<ClassOrInterface> strategies
    ) {
        super();
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

        HasContext hasContext = new HasContext(strategy);
        hasContext.setContext(this.context);

        HasInterface hasInterface = new HasInterface(hasContext);
        hasInterface.setInterfaceName(this.strategyInterface);

        return hasInterface;
    }
}
