package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.Result;
import nl.windesheim.reporting.components.TreeBuilder;

/**
 * The base class for decorating a Found Pattern report.
 */
abstract public class FoundPatternReportDecorator implements IFoundPatternReport {

    /**
     * FoundPatternReport
     */
    protected IFoundPatternReport foundPatternReport;

    /**
     * Default constructor.
     * @param foundPatternReport the decorator
     */
    public FoundPatternReportDecorator(final IFoundPatternReport foundPatternReport) {
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
}
