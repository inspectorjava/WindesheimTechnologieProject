package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * Has classes report decorator.
 */
public class HasClassList extends FoundPatternReportDecorator {

    /**
     * List of classes.
     */
    private List<ClassOrInterface> classes;

    /**
     * The name of the class collection.
     */
    private String name;

    /**
     * Default constructor.
     * @param patternFound the decorator
     */
    public HasClassList(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set classes.
     * @param classes list of classes
     */
    public void setClasses(final List<ClassOrInterface> classes) {
        this.classes = classes;
    }

    /**
     * @return name of the class list
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name of the class list
     * @return this
     */
    public HasClassList setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Append the string with all classes.
     * @return appended string
     */
    public String getReport() {
        StringBuilder baseString = new StringBuilder(super.getReport());

        baseString.append(name).append(":\n\r");
        for (ClassOrInterface classObject : this.classes) {
            baseString.append("- " + classObject.getName() + "\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode(name);
        node.setNodeType(NodeType.CLASS_LIST);
        for (ClassOrInterface classObject : this.classes) {
            node.addChild(new TreeNode(classObject.getName())
                .setNodeType(NodeType.CLASS)
                .setClassOrInterface(classObject)
            );
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
