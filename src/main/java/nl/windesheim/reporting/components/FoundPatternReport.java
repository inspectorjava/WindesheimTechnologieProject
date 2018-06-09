package nl.windesheim.reporting.components;

import nl.windesheim.reporting.DesignPatternType;

import java.util.ArrayList;
import java.util.List;

/**
 * Found a pattern. Report it
 */
public class FoundPatternReport implements IFoundPatternReport {

    /**
     * Result.
     */
    private final Result result;

    /**
     * String pattern type i.e. Singleton, Strategy.
     */
    private DesignPatternType designPatternType;

    /**
     * A list of reasons why the pattern is partially found.
     */
    private final List<String> patternErrors = new ArrayList<>();

    /**
     * Default constructor.
     */
    public FoundPatternReport() {
        this.result = new Result();
        this.result.setCertainty(Result.Certainty.CERTAIN);
        this.designPatternType = DesignPatternType.NONE;
    }

    /**
     * Set the design pattern type of the found design pattern.
     *
     * @param designPatternType design pattern type enum
     */
    public void setDesignPatternType(final DesignPatternType designPatternType) {
        this.designPatternType = designPatternType;
    }

    /**
     * Returns the design pattern type.
     *
     * @return DesignPatternType enum
     */
    public DesignPatternType getDesignPatternType() {
        return this.designPatternType;
    }

    /**
     * @return a list of errors with the design pattern
     */
    public List<String> getPatternErrors() {
        return patternErrors;
    }

    /**
     * @param error a error in the found design pattern
     * @return this
     */
    public FoundPatternReport addPatternError(final String error) {
        patternErrors.add(error);
        return this;
    }

    /**
     * @param certainty the certainty of the detected pattern
     * @return this
     */
    public FoundPatternReport setCertainty(final Result.Certainty certainty) {
        this.result.setCertainty(certainty);
        return this;
    }

    /**
     * Get report.
     *
     * @return String report result
     */
    public String getReport() {
        String report = "Pattern: " + this.designPatternType.toString() + " found with certainty: " + this.result
                .toString();

        StringBuilder reportBuffer = new StringBuilder(report.length());
        reportBuffer.append(report);

        if (!patternErrors.isEmpty()) {
            reportBuffer.append(" with the following errors: \n");

            for (String error : patternErrors) {
                reportBuffer.append(" - " + error + "\n");
            }
        }

        return reportBuffer.toString();
    }

    @Override
    public TreeBuilder buildTreeReport(final TreeBuilder builder) {
        TreeNode node = new TreeNode("Pattern: " + this.designPatternType);
        node.setNodeType(NodeType.DESIGN_PATTERN);
        node.setResultCertainty(this.result.getResult());

        for (String error : patternErrors) {
            TreeNode errorNode = new TreeNode(error);
            errorNode.setNodeType(NodeType.PATTERN_ERROR);
            node.addChild(errorNode);
        }

        builder.setRoot(node);
        return builder;
    }
}
