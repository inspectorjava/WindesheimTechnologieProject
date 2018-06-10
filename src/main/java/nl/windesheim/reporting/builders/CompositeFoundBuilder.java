package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.patterns.CompositePattern;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasClassList;
import nl.windesheim.reporting.decorators.HasComponent;

/**
 * Composite found builder.
 */
public class CompositeFoundBuilder extends AbstractFoundPatternBuilder {
    /**
     * The component of the composite pattern.
     */
    private final CompositePattern pattern;

    /**
     * Default constructor.
     *
     * @param pattern the composite pattern
     */
    public CompositeFoundBuilder(
            final CompositePattern pattern
    ) {
        super();
        this.pattern = pattern;
    }


    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.COMPOSITE);

        HasComponent hasComponent = new HasComponent(patternReport);
        hasComponent.setComponent(pattern.getComponent());

        HasClassList hasComposites = new HasClassList(hasComponent);
        hasComposites.setName("Composites");
        hasComposites.setClasses(pattern.getComposites());

        HasClassList hasLeafs = new HasClassList(hasComposites);
        hasLeafs.setName("Leafs");
        hasLeafs.setClasses(pattern.getLeafs());

        return hasLeafs;
    }
}
