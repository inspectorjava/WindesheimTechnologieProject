package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * Has strategies report decorator
 */
public class HasLinks extends FoundPatternReportDecorator {

    /**
     * List of strategies
     */
    private List<String> links;


    /**
     * Default constructor.
     * @param foundPatternReport the decorator
     */
    public HasLinks(final IFoundPatternReport foundPatternReport) {
        super(foundPatternReport);
    }

    /**
     * Set links
     * @param links list of links
     */
    public void setLinks(List<String> links) {
        this.links = links;
    }

    /**
     * Append the string with all links
     * @return appended string
     */
    public String getReport() {
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (String strategy : this.links) {
            baseString.append("Link: ").append(strategy).append("\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(TreeBuilder builder) {
        TreeNode node = new TreeNode("Links");
        for (String link : this.links) {
            node.addChild(new TreeNode(link));
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
