package nl.windesheim.reporting.components;

import nl.windesheim.reporting.DesignPatternType;

/**
 * Found a pattern. Report it
 */
public class FoundPatternReport implements IFoundPatternReport{

    /**
     * Result.
     */
    private final Result result;

    /**
     * String pattern type i.e. Singleton, Strategy.
     */
    private DesignPatternType designPatternType;


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

    @Override
    public TreeBuilder buildTreeReport(TreeBuilder builder) {
        TreeNode node = new TreeNode("Strategy: " + this.designPatternType);
        builder.setRoot(node);
        return builder;
    }
}
