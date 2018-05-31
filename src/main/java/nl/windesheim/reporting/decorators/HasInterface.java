package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

/**
 * HasInterface report decorator.
 */
public class HasInterface extends FoundPatternReportDecorator {

    /**
     * interface Name.
     */
    private ClassOrInterface interfaceName;

    /**
     * Default constructor.
     * @param patternFound decorator.
     */
    public HasInterface(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set the interface name.
     * @param interfaceName the interface name.
     */
    public void setInterfaceName(final ClassOrInterface interfaceName) {
        this.interfaceName = interfaceName;
    }

    /**
     * Return report part for the interface.
     * @return interface report.
     */
    public String getReport() {
        return super.getReport() + " and uses interface: " + this.interfaceName.getName();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("INTERFACE: " + this.interfaceName.getName())
                .setClassOrInterface(interfaceName)
                .setNodeType(NodeType.INTERFACE);
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
