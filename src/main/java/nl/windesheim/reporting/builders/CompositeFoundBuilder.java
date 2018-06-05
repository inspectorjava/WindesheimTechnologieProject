package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasComponent;
import nl.windesheim.reporting.decorators.HasComposites;
import nl.windesheim.reporting.decorators.HasLeafs;

import java.util.List;

/**
 * Composite found builder.
 */
public class CompositeFoundBuilder extends AbstractFoundPatternBuilder {
    /**
     * The component of the composite pattern.
     */
    private final ClassOrInterface component;

    /**
     * The composites in the composite pattern.
     */
    private final List<ClassOrInterface> composites;

    /**
     * The leafs in the composite pattern.
     */
    private final List<ClassOrInterface> leafs;

    /**
     * Default constructor.
     * @param component the component interface
     * @param composites the composite classes
     * @param leafs the leaf classes
     */
    public CompositeFoundBuilder(
            final ClassOrInterface component,
            final List<ClassOrInterface> composites,
            final List<ClassOrInterface> leafs
    ) {
        super();
        this.component = component;
        this.composites = composites;
        this.leafs = leafs;
    }


    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.COMPOSITE);

        HasComponent hasComponent = new HasComponent(patternReport);
        hasComponent.setComponent(component);

        HasComposites hasComposites = new HasComposites(hasComponent);
        hasComposites.setComposites(composites);

        HasLeafs hasLeafs = new HasLeafs(hasComposites);
        hasLeafs.setLeafs(leafs);

        return hasLeafs;
    }
}
