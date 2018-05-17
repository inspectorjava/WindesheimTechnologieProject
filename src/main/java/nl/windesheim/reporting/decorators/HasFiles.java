package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

import java.util.List;

/**
 * HasFiles report decorator
 */
public class HasFiles extends FoundPatternReportDecorator{

    private List<String> files;

    /**
     * Found pattern report.
     * @param foundPatternReport the decorator
     */
    public HasFiles(final IFoundPatternReport foundPatternReport) {
        super(foundPatternReport);
    }

    /**
     * Set the files
     * @param files
     */
    public void setFiles(final List<String> files) {
        this.files = files;
    }

    /**
     *
     * @return
     */
    public String getReport() {
        StringBuilder reportString = new StringBuilder(super.getReport());
        for (String file : this.files) {
            reportString.append("File: ")
                    .append(file)
                    .append("\n\r");
        }
        return reportString.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(TreeBuilder builder) {
        TreeNode node = new TreeNode();
        node.setName("Files");

        for (String file : this.files) {
            node.addChild(new TreeNode(file));
        }

        builder.addNode(node);

        return super.buildTreeReport(builder);
    }
}
