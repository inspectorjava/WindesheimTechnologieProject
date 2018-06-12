package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Has implementations report decorator.
 */
public class HasFactoryImplementations extends FoundPatternReportDecorator {

    /**
     * List of implementations.
     */
    private HashMap<ClassOrInterface, List<ClassOrInterface>> implementations;


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
    public void setImplementations(final HashMap<ClassOrInterface, List<ClassOrInterface>> implementations) {
        this.implementations = implementations;
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Factory implementations");

        for (Map.Entry<ClassOrInterface, List<ClassOrInterface>>
                entry : this.implementations.entrySet()){
            TreeNode treeNode = new TreeNode(entry.getKey().getName())
                    .setClassOrInterface(entry.getKey())
                    .setNodeType(NodeType.INTERFACE);

            for (ClassOrInterface declaration : entry.getValue()) {
                treeNode.addChild(
                        new TreeNode(declaration.getName())
                        .setClassOrInterface(declaration)
                        .setNodeType(NodeType.CLASS)
                );
            }

            node.addChild(treeNode);
        }
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
        for (Map.Entry<ClassOrInterface, List<ClassOrInterface>>
                entry : this.implementations.entrySet()) {
            baseString.append("factory implementation: ").append(entry.getKey()).append("\n\r");
            for (ClassOrInterface declaration : entry.getValue()) {
                baseString.append("- ").append(declaration.getName()).append("\n\r");
            }
        }

        return baseString.toString();
    }
}
