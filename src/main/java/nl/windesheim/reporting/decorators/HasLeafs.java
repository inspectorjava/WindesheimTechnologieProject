package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * Has leafs report decorator.
 */
public class HasLeafs extends FoundPatternReportDecorator {

    /**
     * List of leafs.
     */
    private List<ClassOrInterface> leafs;


    /**
     * Default constructor.
     * @param patternFound the decorator
     */
    public HasLeafs(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set leafs.
     * @param leafs list of leafs
     */
    public void setLeafs(final List<ClassOrInterface> leafs) {
        this.leafs = leafs;
    }

    /**
     * Append the string with all leafs.
     * @return appended string
     */
    public String getReport() {
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (ClassOrInterface link : this.leafs) {
            baseString.append("Leaf: ").append(link.getName()).append("\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Leafs");
        node.setNodeType(NodeType.CLASS_LIST);
        for (ClassOrInterface link : this.leafs) {
            node.addChild(new TreeNode(link.getName())
                .setClassOrInterface(link)
                .setNodeType(NodeType.CLASS)
            );
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
