package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * Has composites report decorator.
 */
public class HasComposites extends FoundPatternReportDecorator {

    /**
     * List of composites.
     */
    private List<ClassOrInterface> composites;


    /**
     * Default constructor.
     * @param patternFound the decorator
     */
    public HasComposites(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set composites.
     * @param composites list of composites
     */
    public void setComposites(final List<ClassOrInterface> composites) {
        this.composites = composites;
    }

    /**
     * Append the string with all composites.
     * @return appended string
     */
    public String getReport() {
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (ClassOrInterface link : this.composites) {
            baseString.append("Composite: ").append(link.getName()).append("\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Composites");
        node.setNodeType(NodeType.CLASS_LIST);
        for (ClassOrInterface link : this.composites) {
            node.addChild(new TreeNode(link.getName())
                .setClassOrInterface(link)
                .setNodeType(NodeType.CLASS)
            );
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
