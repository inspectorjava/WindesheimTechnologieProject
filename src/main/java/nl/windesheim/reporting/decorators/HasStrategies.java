package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.IFoundPatternReport;

import java.util.List;

/**
 * Has strategies report decorator
 */
public class HasStrategies extends FoundPatternReportDecorator {

    /**
     * List of strategies
     */
    private List<String> strategies;


    /**
     * Default constructor.
     * @param foundPatternReport the decorator
     */
    public HasStrategies(final IFoundPatternReport foundPatternReport) {
        super(foundPatternReport);
    }

    /**
     * Set strategies
     * @param strategies list of strategies
     */
    public void setStrategies(List<String> strategies) {
        this.strategies = strategies;
    }

    /**
     * Append the string with all strategies
     * @return appended string
     */
    public String getReport() {
        StringBuilder baseString = new StringBuilder(super.getReport());
        for (String strategy : this.strategies) {
            baseString.append("Stratagy: ").append(strategy).append("\n\r");
        }

        return baseString.toString();
    }
}
