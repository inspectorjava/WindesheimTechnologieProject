package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * Has strategies report decorator.
 */
public class HasLinks extends FoundPatternReportDecorator {

    /**
     * List of strategies.
     */
    private List<ClassOrInterface> links;


    /**
     * Default constructor.
     * @param patternFound the decorator
     */
    public HasLinks(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set links.
     * @param links list of links
     */
    public void setLinks(final List<ClassOrInterface> links) {
        this.links = links;
    }

    /**
     * Append the string with all links.
     * @return appended string
     */
    public String getReport() {
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (ClassOrInterface link : this.links) {
            baseString.append("Link: ").append(link.getName()).append("\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Links");
        node.setNodeType(NodeType.CLASS_LIST);
        for (ClassOrInterface link : this.links) {
            node.addChild(new TreeNode(link.getName())
                .setClassOrInterface(link)
                .setNodeType(NodeType.CLASS)
            );
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
