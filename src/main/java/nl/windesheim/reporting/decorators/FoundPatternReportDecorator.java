package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.TreeBuilder;

/**
 * The base class for decorating a Found Pattern report.
 */
public abstract class FoundPatternReportDecorator implements IFoundPatternReport {

    /**
     * FoundPatternReport.
     */
    private IFoundPatternReport foundPattern;

    /**
     * Default constructor.
     * @param foundPattern the decorator
     */
    public FoundPatternReportDecorator(final IFoundPatternReport foundPattern) {
        this.foundPattern = foundPattern;
    }

    /**
     * Return the report.
     * @return string
     */
    public String getReport() {
        return this.foundPattern.getReport();
    }

    /**
     * Build report tree.
     * @param builder TreeBuilder
     * @return TreeBuilder
     */
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        return this.foundPattern.buildTreeReport(builder);
    }

    /**
     * Get found pattern report.
     * @return foundPattern
     */
    protected IFoundPatternReport getFoundPatternReport() {
        return foundPattern;
    }

    /**
     * Set found pattern report.
     * @param foundPattern the found pattern report.
     */
    protected void setFoundPatternReport(final IFoundPatternReport foundPattern) {
        this.foundPattern = foundPattern;
    }
}
