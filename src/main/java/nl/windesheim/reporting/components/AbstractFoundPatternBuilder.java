package nl.windesheim.reporting.components;

import nl.windesheim.reporting.DesignPatternType;

/**
 * Extend this class to create a new found pattern build.
 */
public abstract class AbstractFoundPatternBuilder implements IFoundPatternBuilder {
    /**
     * Design patterns.
     */
    private DesignPatternType designPatternType;

    /**
     * Set the design pattern.
     * @param designPatternType design pattern type.
     */
    public void setDesignPatternType(final DesignPatternType designPatternType) {
        this.designPatternType = designPatternType;
    }

    /**
     * Gets the design pattern type.
     * @return designPatternType design pattern type.
     */
    public DesignPatternType getDesignPatternType() {
        return designPatternType;
    }
}
