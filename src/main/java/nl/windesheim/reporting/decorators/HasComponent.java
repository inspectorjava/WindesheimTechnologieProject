package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

/**
 * Has component decorator.
 */
public class HasComponent extends FoundPatternReportDecorator {

    /**
     * The component of found design pattern report.
     */
    private ClassOrInterface component;

    /**
     * Decorator constructor.
     * @param foundPattern the decorator.
     */
    public HasComponent(final IFoundPatternReport foundPattern) {
        super(foundPattern);
    }

    /**
     * Set the component of the file.
     * @param component the component
     */
    public void setComponent(final ClassOrInterface component) {
        this.component = component;
    }

    /**
     * Report component of given pattern.
     * @return String component
     */
    public String getReport() {
        return super.getReport() + " - Component: " + this.component.getName() + "\n\r";
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Component: " + this.component.getName())
                .setNodeType(NodeType.CLASS)
                .setClassOrInterface(component);
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}

