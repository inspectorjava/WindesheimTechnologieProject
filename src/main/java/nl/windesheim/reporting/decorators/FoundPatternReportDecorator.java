package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.Result;

/**
 * The base class for decorating a Found Pattern report.
 */
abstract public class FoundPatternReportDecorator implements IFoundPatternReport {
    /**
     * String pattern type i.e. Singleton, Strategy.
     */
    private DesignPatternType designPatternType;

    /**
     * Result.
     */
    private final Result result;

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
        this.result = new Result();
        this.result.setCertainty(Result.Certainty.NOT);
        this.designPatternType = DesignPatternType.NONE;
    }

    /**
     * Set the design pattern type of the found design pattern.
     * @param designPatternType design pattern type enum
     */
    public void setDesignPatternType(final DesignPatternType designPatternType) {
        this.designPatternType = designPatternType;
    }

    /**
     * Returns the design pattern type.
     * @return DesignPatternType enum
     */
    public DesignPatternType getDesignPatternType() {
        return this.designPatternType;
    }

    /**
     * Get report.
     * @return String report result
     */
    public String getReport() {
        return "Pattern: " + this.designPatternType.toString() + " found with certainty: " + this.result.toString();
    }
}
