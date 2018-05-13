package nl.windesheim.reporting.components;

import nl.windesheim.reporting.DesignPatternType;

/**
 * Found a pattern. Report it
 */
public class FoundPatternReport {

    /**
     * String pattern type i.e. Singleton, Strategy.
     */
    private DesignPatternType designPatternType;

    /**
     * Result.
     */
    private final Result result;

    /**
     * Constructor.
     */
    public FoundPatternReport() {
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
