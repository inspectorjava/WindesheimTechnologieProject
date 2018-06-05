package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * Has concrete observables report decorator.
 */
public class HasConcreteObservables extends FoundPatternReportDecorator {

    /**
     * List of concrete observables.
     */
    private List<ClassOrInterface> cObservables;


    /**
     * Default constructor.
     * @param patternFound the decorator
     */
    public HasConcreteObservables(final IFoundPatternReport patternFound) {
        super(patternFound);
    }

    /**
     * Set concrete observables.
     * @param cObservables list of concrete observables
     */
    public void setConcreteObservables(final List<ClassOrInterface> cObservables) {
        this.cObservables = cObservables;
    }

    /**
     * Append the string with all concrete observables.
     * @return appended string
     */
    public String getReport() {
        //Can't fix this without magic numbers which are also illegal
        @SuppressWarnings("PMD.InsufficientStringBufferDeclaration")
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (ClassOrInterface observable : this.cObservables) {
            baseString.append("Concrete Observable: ").append(observable.getName()).append("\n\r");
        }

        return baseString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Concrete Observables");
        node.setNodeType(NodeType.CLASS_LIST);
        for (ClassOrInterface link : this.cObservables) {
            node.addChild(new TreeNode(link.getName())
                .setClassOrInterface(link)
                .setNodeType(NodeType.CLASS)
            );
        }
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
