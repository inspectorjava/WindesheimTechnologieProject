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
    private IFoundPatternReport foundPatternReport;

    /**
     * Default constructor.
     * @param foundPatternReport the decorator
     */
    FoundPatternReportDecorator(final IFoundPatternReport foundPatternReport) {
        this.foundPatternReport = foundPatternReport;
    }

    /**
     * Return the report.
     * @return string
     */
    public String getReport() {
        return this.foundPatternReport.getReport();
    }

    /**
     * Build report tree.
     * @param builder TreeBuilder
     * @return TreeBuilder
     */
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        return this.foundPatternReport.buildTreeReport(builder);
    }

    /**
     * Get found pattern report.
     * @return foundPatternReport
     */
    protected IFoundPatternReport getFoundPatternReport() {
        return foundPatternReport;
    }

    /**
     * Set found pattern report.
     * @param foundPatternReport the found pattern report.
     */
    protected void setFoundPatternReport(final IFoundPatternReport foundPatternReport) {
        this.foundPatternReport = foundPatternReport;
    }
}
