package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

/**
 * The found pattern has a context.
 */
public class HasAbstractObservable extends FoundPatternReportDecorator {

    /**
     * The abstract aObservable of found design pattern report.
     */
    private ClassOrInterface aObservable;

    /**
     * Decorator constructor.
     * @param foundPattern the decorator.
     */
    public HasAbstractObservable(final IFoundPatternReport foundPattern) {
        super(foundPattern);
    }

    /**
     * Set the aObservable of the pattern.
     * @param aObservable the aObservable
     */
    public void setAbstractObservable(final ClassOrInterface aObservable) {
        this.aObservable = aObservable;
    }

    /**
     * Report aObservable of given pattern.
     * @return String aObservable
     */
    public String getReport() {
        return super.getReport() + " - Abstract Observable: " + this.aObservable.getName() + "\n\r";
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        NodeType nodeType;
        if (aObservable.getDeclaration().isInterface()) {
            nodeType = NodeType.INTERFACE;
        } else {
            nodeType = NodeType.CLASS;
        }

        TreeNode node = new TreeNode("Abstract Observable: " + this.aObservable.getName())
                .setNodeType(nodeType)
                .setClassOrInterface(aObservable);
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
