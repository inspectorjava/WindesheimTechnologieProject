package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

/**
 * The found pattern has a context.
 */
public class HasAbstractObserver extends FoundPatternReportDecorator {

    /**
     * The context of found design pattern report.
     */
    private ClassOrInterface aObserver;

    /**
     * Decorator constructor.
     * @param foundPattern the decorator.
     */
    public HasAbstractObserver(final IFoundPatternReport foundPattern) {
        super(foundPattern);
    }

    /**
     * Set the aObserver of the pattern.
     * @param aObserver the aObserver
     */
    public void setAbstractObserver(final ClassOrInterface aObserver) {
        this.aObserver = aObserver;
    }

    /**
     * Report aObserver of given pattern.
     * @return String aObserver
     */
    public String getReport() {
        return super.getReport() + " - Abstract Observer: " + this.aObserver.getName() + "\n\r";
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        NodeType nodeType = aObserver.getDeclaration().isInterface()
                ? NodeType.INTERFACE
                : NodeType.CLASS;

        TreeNode node = new TreeNode("Observer: " + this.aObserver.getName())
                .setNodeType(nodeType)
                .setClassOrInterface(aObserver);
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
