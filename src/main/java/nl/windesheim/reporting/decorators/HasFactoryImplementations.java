package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * Has implementations report decorator.
 */
public class HasFactoryImplementations extends FoundPatternReportDecorator {

    /**
     * List of implementations.
     */
    private List<ClassOrInterface> implementations;


    /**
     * Default constructor.
     * @param patternFound the decorator
     */
    public HasFactoryImplementations(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set implementations.
     * @param implementations list of implementations
     */
    public void setImplementations(final List<ClassOrInterface> implementations) {
        this.implementations = implementations;
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Factory implementations");

        for (ClassOrInterface link : this.implementations) {
            node.addChild(new TreeNode(link.getName())
                .setClassOrInterface(link)
                .setNodeType(NodeType.CLASS)
            );
        }
        node.setNodeType(NodeType.CLASS_LIST);
        builder.addNode(node);

        return super.buildTreeReport(builder);
    }

    /**
     * Append the string with all implementations.
     * @return appended string
     */
    public String getReport() {
        //Can't fix this without magic numbers which are also illegal
        @SuppressWarnings("PMD.InsufficientStringBufferDeclaration")
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (ClassOrInterface link : this.implementations) {
            baseString.append("Factory implementation: ").append(link.getName()).append("\n\r");
        }

        return baseString.toString();
    }
}
