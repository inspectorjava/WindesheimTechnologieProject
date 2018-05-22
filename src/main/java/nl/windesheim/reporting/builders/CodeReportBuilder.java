package nl.windesheim.reporting.builders;

import nl.windesheim.reporting.components.CodeReport;
import nl.windesheim.reporting.components.IFoundPatternBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Build a CodeReport objects.
 */
public class CodeReportBuilder {

    /**
     * All found pattern builders.
     */
    private final List<IFoundPatternBuilder> foundBuilders;

    /**
     * Instantiate the arraylist.
     */
    public CodeReportBuilder() {
        this.foundBuilders = new ArrayList<IFoundPatternBuilder>();
    }

    /**
     * Add a foundPatternBuilder to the array.
     * @param foundBuilder foundPatternBuilder
     */
    public void addFoundPatternBuilder(final IFoundPatternBuilder foundBuilder) {
        this.foundBuilders.add(foundBuilder);
    }

    /**
     * Build the code report.
     * @return CodeReport
     */
    public CodeReport buildReport() {
        CodeReport codeReport = new CodeReport();
        for (IFoundPatternBuilder foundBuilder : this.foundBuilders) {
            codeReport.addFoundPatternReport(foundBuilder.buildReport());
        }
        return codeReport;
    }
}
