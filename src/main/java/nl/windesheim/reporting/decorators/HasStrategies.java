package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.IFoundPatternReport;
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
    private List<String> strategies;


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
    public void setStrategies(final List<String> strategies) {
        this.strategies = strategies;
    }

    /**
     * Append the string with all strategies.
     * @return appended string
     */
    public String getReport() {
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (String strategy : this.strategies) {
            baseString.append("Strategy: ").append(strategy).append("\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Strategies");
        for (String strategy : this.strategies) {
            node.addChild(new TreeNode(strategy));
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
