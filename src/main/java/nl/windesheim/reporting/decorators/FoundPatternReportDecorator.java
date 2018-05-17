package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.Result;

/**
 * The base class for decorating a Found Pattern report.
 */
abstract public class FoundPatternReportDecorator implements IFoundPatternReport {

    /**
     * FoundPatternReport
     */
    protected IFoundPatternReport foundPatternReport;

    /**
     * Default constructor
     * @param foundPatternReport the decorator
     */
    public FoundPatternReportDecorator(final IFoundPatternReport foundPatternReport) {
        this.foundPatternReport = foundPatternReport;
    }

    public String getReport() {
        return this.foundPatternReport.getReport();
    }
}
