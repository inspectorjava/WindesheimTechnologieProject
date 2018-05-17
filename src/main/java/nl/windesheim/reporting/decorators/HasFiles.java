package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.IFoundPatternReport;

import java.util.List;

/**
 * HasFiles report decorator
 */
public class HasFiles extends FoundPatternReportDecorator{

    private List<String> files;

    /**
     * Found pattern report.
     * @param foundPatternReport the decorator
     */
    public HasFiles(final IFoundPatternReport foundPatternReport) {
        super(foundPatternReport);
    }

    /**
     * Set the files
     * @param files
     */
    public void setFiles(final List<String> files) {
        this.files = files;
    }

    /**
     *
     * @return
     */
    public String getReport() {
        String reportString = super.getReport();
        for (String file : this.files) {
            reportString += "File: " + file + "\n\r";
        }
        return reportString;
    }
}
