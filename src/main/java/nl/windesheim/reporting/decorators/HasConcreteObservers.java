package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * Has concrete observers report decorator.
 */
public class HasConcreteObservers extends FoundPatternReportDecorator {

    /**
     * List of concrete observers.
     */
    private List<ClassOrInterface> cObservers;


    /**
     * Default constructor.
     * @param patternFound the decorator
     */
    public HasConcreteObservers(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set concrete observers.
     * @param cObservers list of concrete observers
     */
    public void setConcreteObservers(final List<ClassOrInterface> cObservers) {
        this.cObservers = cObservers;
    }

    /**
     * Append the string with all concrete observers.
     * @return appended string
     */
    public String getReport() {
        //Can't fix this without magic numbers which are also illegal
        @SuppressWarnings("PMD.InsufficientStringBufferDeclaration")
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (ClassOrInterface observer : this.cObservers) {
            baseString.append("Concrete Observer: ").append(observer.getName()).append("\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Concrete Observers");
        node.setNodeType(NodeType.CLASS_LIST);
        for (ClassOrInterface link : this.cObservers) {
            node.addChild(new TreeNode(link.getName())
                .setClassOrInterface(link)
                .setNodeType(NodeType.CLASS)
            );
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
