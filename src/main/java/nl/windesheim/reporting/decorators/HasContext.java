package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

/**
 * The found pattern has a context.
 */
public class HasContext extends FoundPatternReportDecorator {

    /**
     * The context of found design pattern report.
     */
    private String context;

    /**
     * Decorator constructor.
     * @param foundPatternReport the decorator.
     */
    public HasContext(final IFoundPatternReport foundPatternReport) {
        super(foundPatternReport);
    }

    /**
     * Set the context of the file.
     * @param context the context
     */
    public void setContext(final String context) {
        this.context = context;
    }

    /**
     * Report context of given pattern.
     * @return String context
     */
    public String getReport() {
        return super.getReport() + " - Context: " + this.context + "\n\r";
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Context: " + this.context);
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
