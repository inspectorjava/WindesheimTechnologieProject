package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.patterns.AbstractFactory;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasFactoryImplementations;
import nl.windesheim.reporting.decorators.HasClassList;
import nl.windesheim.reporting.decorators.HasClassOrInterface;

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
        HasClassOrInterface hasInterface = new HasClassOrInterface(patternReport);
        hasInterface.setName("Factory interface");
        hasInterface.setClassOrInterface(this.pattern.getFactoryInterface());

        HasClassList hasImpl = new HasClassList(hasInterface);
        hasImpl.setName("Implementations");
        hasImpl.setClasses(this.pattern.getImplementations());

        HasFactoryImplementations hasFacImpl = new HasFactoryImplementations(hasImpl);
        hasFacImpl.setImplementations(this.pattern.getConcreteImplementations());

        return hasFacImpl;
    }
}
