package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * Has command receivers report decorator.
 */
public class HasReceivers extends FoundPatternReportDecorator {

    /**
     * List of receivers.
     */
    private List<ClassOrInterface> receivers;


    /**
     * Default constructor.
     * @param patternFound the command receivers
     */
    public HasReceivers(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set receivers.
     * @param receivers list of receivers
     */
    public void setReceivers(final List<ClassOrInterface> receivers) {
        this.receivers = receivers;
    }

    /**
     * Append the string with all receivers.
     * @return appended string
     */
    public String getReport() {
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (ClassOrInterface receiver : this.receivers) {
            baseString.append("Receiver: ").append(receiver.getName()).append("\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Receivers");
        for (ClassOrInterface receiver : this.receivers) {
            node.addChild(
                    new TreeNode(receiver.getName())
                        .setClassOrInterface(receiver)
                        .setNodeType(NodeType.CLASS)
            );
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
