package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * Has command receivers report decorator.
 */
public class HasReceivers extends FoundPatternReportDecorator {

    /**
     * List of receivers.
     */
    private List<String> links;


    /**
     * Default constructor.
     * @param patternFound the command receivers
     */
    public HasReceivers(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set links.
     * @param links list of links
     */
    public void setLinks(final List<String> links) {
        this.links = links;
    }

    /**
     * Append the string with all links.
     * @return appended string
     */
    public String getReport() {
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (String strategy : this.links) {
            baseString.append("Receiver: ").append(strategy).append("\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Receivers");
        for (String link : this.links) {
            node.addChild(new TreeNode(link));
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
