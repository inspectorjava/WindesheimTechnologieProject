package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * Has command report decorator.
 */
public class HasCommands extends FoundPatternReportDecorator {

    /**
     * List of commands.
     */
    private List<ClassOrInterface> commands;


    /**
     * Default constructor.
     * @param patternFound the decorator
     */
    public HasCommands(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set commands.
     * @param commands list of commands
     */
    public void setCommands(final List<ClassOrInterface> commands) {
        this.commands = commands;
    }

    /**
     * Append the string with all commands.
     * @return appended string
     */
    public String getReport() {
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (ClassOrInterface command : this.commands) {
            baseString.append("Command: ").append(command.getName()).append("\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Commands");
        node.setNodeType(NodeType.CLASS_LIST);
        for (ClassOrInterface command : this.commands) {
            node.addChild(
                    new TreeNode(command.getName())
                        .setNodeType(NodeType.CLASS)
                        .setClassOrInterface(command)
            );
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
