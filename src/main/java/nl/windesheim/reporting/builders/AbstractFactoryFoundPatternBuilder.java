package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.patterns.AbstractFactory;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasClassList;
import nl.windesheim.reporting.decorators.HasInterface;

/**
 * The abstract factory found pattern builder.
 */
public class AbstractFactoryFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * The factory pattern.
     */
    private final AbstractFactory pattern;

    /**
     * The constructor.
     * @param pattern factory pattern.
     */
    public AbstractFactoryFoundPatternBuilder(final AbstractFactory pattern) {
        super();

        this.pattern = pattern;
    }

    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.ABSTRACT_FACTORY);
        HasInterface hasInterface = new HasInterface(patternReport);
        hasInterface.setInterface(this.pattern.getFactoryInterface());

        HasClassList hasImpl = new HasClassList(hasInterface);
        hasImpl.setName("Implementations");
        hasImpl.setClasses(this.pattern.getImplementations());

        return hasImpl;
    }
}
