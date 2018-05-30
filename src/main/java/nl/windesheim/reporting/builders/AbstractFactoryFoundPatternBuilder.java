package nl.windesheim.reporting.builders;

import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasContext;

public class AbstractFactoryFoundPatternBuilder extends AbstractFoundPatternBuilder {
    private final String factoryName;

    public AbstractFactoryFoundPatternBuilder(String factoryName) {
        super();
        this.factoryName = factoryName;
    }

    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.ABSTRACT_FACTORY);
        HasContext hasContext = new HasContext(patternReport);
        hasContext.setContext(this.factoryName);
        return hasContext;
    }
}
