package nl.windesheim.reporting.builders;

import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasInterface;
import nl.windesheim.reporting.decorators.HasLinks;

import java.util.List;

/**
 * Chain Of Responsibility pattern builder.
 */
public class ChainOfResponsibilityFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * Filename of file where pattern is found.
     */
    private final String commonParent;

    /**
     * List of links.
     */
    private final List<String> links;

    /**
     * Create the builder.
     * @param commonParent filename of the file where singleton is found
     * @param links Links of the chain of responsiblity pattern
     */
    public ChainOfResponsibilityFoundPatternBuilder(final String commonParent, final List<String> links) {
        super();
        this.commonParent = commonParent;
        this.links = links;
    }

    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.CHAIN_OF_RESPONSIBILITY);
        HasInterface hasInterface = new HasInterface(patternReport);
        hasInterface.setInterfaceName(this.commonParent);

        HasLinks hasLinks = new HasLinks(hasInterface);
        hasLinks.setLinks(this.links);
        return hasLinks;
    }
}
