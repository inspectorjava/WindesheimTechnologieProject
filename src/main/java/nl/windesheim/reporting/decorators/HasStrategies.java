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
public class HasStrategies extends FoundPatternReportDecorator {

    /**
     * List of strategies.
     */
    private List<ClassOrInterface> strategies;


    /**
     * Default constructor.
     * @param patternFound the decorator
     */
    public HasStrategies(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set strategies.
     * @param strategies list of strategies
     */
    public void setStrategies(final List<ClassOrInterface> strategies) {
        this.strategies = strategies;
    }

    /**
     * Append the string with all strategies.
     * @return appended string
     */
    public String getReport() {
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (ClassOrInterface strategy : this.strategies) {
            baseString.append("Strategy: ").append(strategy.getName()).append("\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Strategies");
        node.setNodeType(NodeType.CLASS_LIST);
        for (ClassOrInterface strategy : this.strategies) {
            node.addChild(new TreeNode(strategy.getName())
                .setNodeType(NodeType.CLASS)
                .setClassOrInterface(strategy)
            );
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
