package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

/**
 * HasCommonParen report decorator.
 */
public class HasCommonParent extends FoundPatternReportDecorator {

    /**
     * common parent.
     */
    private ClassOrInterface commonParent;

    /**
     * Default constructor.
     * @param patternFound decorator.
     */
    public HasCommonParent(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set the interface name.
     * @param commonParent the interface name.
     */
    public void setCommonParent(final ClassOrInterface commonParent) {
        this.commonParent = commonParent;
    }

    /**
     * Return report part for the common parent.
     * @return interface report.
     */
    public String getReport() {
        return super.getReport() + " and uses common parent: " + this.commonParent.getName();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Common parent: " + this.commonParent.getName())
                .setClassOrInterface(commonParent);
        if (commonParent.getDeclaration().isInterface()) {
            node.setNodeType(NodeType.INTERFACE);
        } else {
            node.setNodeType(NodeType.ABSTRACT_CLASS);
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
