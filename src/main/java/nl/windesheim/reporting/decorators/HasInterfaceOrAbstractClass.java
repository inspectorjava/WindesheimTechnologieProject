package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

/**
 * HasCommonParen report decorator.
 */
public class HasInterfaceOrAbstractClass extends FoundPatternReportDecorator {

    /**
     * class or interface.
     */
    private ClassOrInterface classOrInterface;

    /**
     * Class or interface name.
     */
    private String name;

    /**
     * Default constructor.
     * @param patternFound decorator.
     */
    public HasInterfaceOrAbstractClass(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set the class or interface name.
     * @param classOrInterface the class or interface.
     */
    public void setClassOrInterface(final ClassOrInterface classOrInterface) {
        this.classOrInterface = classOrInterface;
    }

    /**
     * @return name of class or interface
     */
    public String getName() {
        return name;
    }

    /**
     * @param name name of class or interface
     * @return this
     */
    public HasInterfaceOrAbstractClass setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Return report part for the common parent.
     * @return interface report.
     */
    public String getReport() {
        return super.getReport() + " and uses " + name + ": " + this.classOrInterface.getName();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode(name + ": " + this.classOrInterface.getName())
                .setClassOrInterface(classOrInterface);
        if (classOrInterface.getDeclaration().isInterface()) {
            node.setNodeType(NodeType.INTERFACE);
        } else {
            node.setNodeType(NodeType.ABSTRACT_CLASS);
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
