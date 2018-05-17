package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;

/**
 * The found pattern has a contexxt
 */
public class HasContext extends FoundPatternReportDecorator{

    /**
     * The context of found design pattern report
     */
    private String context;

    /**
     * Decorator constructor.
     * @param foundPatternReport the decorator.
     */
    public HasContext(final IFoundPatternReport foundPatternReport) {
        super(foundPatternReport);
    }

    /**
     * Set the context of the file
     * @param context
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * Report context of given pattern.
     * @return String context
     */
    public String getReport() {
        return super.getReport() + " - Context: " + this.context;
    }
}
