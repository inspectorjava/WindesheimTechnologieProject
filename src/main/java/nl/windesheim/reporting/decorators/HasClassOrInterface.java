package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

/**
 * The found pattern has a class or interface.
 */
public class HasClassOrInterface extends FoundPatternReportDecorator {

    /**
     * The abstract classOrInterface of found design pattern report.
     */
    private ClassOrInterface classOrInterface;

    /**
     * The name of the classOrInterface.
     */
    private String name;

    /**
     * Decorator constructor.
     * @param foundPattern the decorator.
     */
    public HasClassOrInterface(final IFoundPatternReport foundPattern) {
        super(foundPattern);
    }

    /**
     * @return name of the class or interface
     */
    public String getName() {
        return name;
    }

    /**
     * @param name name of class or interface
     * @return this
     */
    public HasClassOrInterface setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the classOrInterface of the pattern.
     * @param classOrInterface the classOrInterface
     */
    public void setClassOrInterface(final ClassOrInterface classOrInterface) {
        this.classOrInterface = classOrInterface;
    }

    /**
     * Report classOrInterface of given pattern.
     * @return String classOrInterface
     */
    public String getReport() {
        return super.getReport() + " - " + name + ": " + this.classOrInterface.getName() + "\n\r";
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        NodeType nodeType;
        if (classOrInterface.getDeclaration().isInterface()) {
            nodeType = NodeType.INTERFACE;
        } else {
            nodeType = NodeType.CLASS;
        }

        TreeNode node = new TreeNode(name + ": " + this.classOrInterface.getName())
                .setNodeType(nodeType)
                .setClassOrInterface(classOrInterface);
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
