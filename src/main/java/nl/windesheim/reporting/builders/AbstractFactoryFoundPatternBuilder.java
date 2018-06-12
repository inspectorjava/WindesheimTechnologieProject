package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.patterns.AbstractFactory;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasFactoryImplementations;
import nl.windesheim.reporting.decorators.HasImplementations;
import nl.windesheim.reporting.decorators.HasInterface;

import java.util.HashMap;
import java.util.List;

/**
 * The abstract factory found pattern builder.
 */
public class AbstractFactoryFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * The factory name to use.
     */
    private final ClassOrInterface factory;

    /**
     * List of the factory implementations.
     */
    private final List<ClassOrInterface> implementations;

    /**
     * List of the implementations used by the factory.
     */
    private final HashMap<ClassOrInterface, List<ClassOrInterface>> factoryImpl;

    /**
     * The constructor.
     * @param factory The name for this factory.
     */
    public AbstractFactoryFoundPatternBuilder(final AbstractFactory factory) {
        super();

        this.factory = factory.getFactoryInterface();
        this.implementations = factory.getImplementations();
        this.factoryImpl = factory.getConcreteImplementations();
    }

    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.ABSTRACT_FACTORY);
        HasInterface hasInterface = new HasInterface(patternReport);
        hasInterface.setInterfaceName(this.factory);

        HasImplementations hasImpl = new HasImplementations(hasInterface);
        hasImpl.setImplementations(this.implementations);

        HasFactoryImplementations hasFacImpl = new HasFactoryImplementations(patternReport);
        hasFacImpl.setImplementations(this.factoryImpl);

        return hasFacImpl;
    }
}
