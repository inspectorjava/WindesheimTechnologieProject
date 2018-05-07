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
    private List<IFoundPatternBuilder> foundPatternBuilders;

    /**
     * Instantiate the arraylist.
     */
    public CodeReportBuilder() {
        this.foundPatternBuilders = new ArrayList<IFoundPatternBuilder>();
    }

    /**
     * Add a foundPatternBuilder to the array.
     * @param foundPatternBuilder foundPatternBuilder
     */
    public void addFoundPatternBuilder(final IFoundPatternBuilder foundPatternBuilder) {
        this.foundPatternBuilders.add(foundPatternBuilder);
    }

    /**
     * Build the code report.
     * @return CodeReport
     */
    public CodeReport buildReport() {
        CodeReport codeReport = new CodeReport();
        for (IFoundPatternBuilder foundPatternBuilder : this.foundPatternBuilders) {
            codeReport.addFoundPatternReport(foundPatternBuilder.buildReport());
        }
        return codeReport;
    }
}
