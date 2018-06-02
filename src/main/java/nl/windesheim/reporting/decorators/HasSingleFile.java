package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.NodeType;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;

/**
 * Decorator for a report that has a single file.
 */
public class HasSingleFile extends FoundPatternReportDecorator {

    /**
     * Filename of found pattern.
     */
    private ClassOrInterface file;

    /**
     * Default constructor.
     * @param report for decorator
     */
    public HasSingleFile(final IFoundPatternReport report) {
        super(report);
    }

    /**
     * Set the filename.
     * @param file pattern was found in this file.
     */
    public void setFile(final ClassOrInterface file) {
        this.file = file;
    }

    /**
     *
     * @return String report string.
     */
    @Override
    public String getReport() {
        return super.getReport() + "\n\r" + "Found in file: " + this.file.getFilePart().getFile();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        NodeType type;

        if (file.getDeclaration().isInterface()) {
            type = NodeType.INTERFACE;
        } else {
            type = NodeType.CLASS;
        }

        TreeNode node = new TreeNode(this.file.getName())
                .setClassOrInterface(file)
                .setNodeType(type);
        builder.addNode(node);
        return super.buildTreeReport(builder);
    }
}
