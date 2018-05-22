package nl.windesheim.reporting.components;

/**
 * Interface for building found pattern classes.
 */
public interface IFoundPatternBuilder {
    /**
     * Return a new FoundPatternReport.
     * @return IFoundPatternReport the report which has been created
     */
    IFoundPatternReport buildReport();
}
