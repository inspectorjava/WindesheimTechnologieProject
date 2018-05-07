package nl.windesheim.reporting.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Get a code report.
 */
public class CodeReport {
    /**
     * Found Pattern Reports.
     */
    private List<FoundPatternReport> foundPatternReports;

    /**
     * Instantiate the foundPatternReports ArrayList.
     */
    public CodeReport() {
        this.foundPatternReports = new ArrayList<FoundPatternReport>();
    }

    /**
     * Add a FoundPatternReport to the CodeReport.
     * @param foundPatternReport the report
     */
    public void addFoundPatternReport(final FoundPatternReport foundPatternReport) {
        this.foundPatternReports.add(foundPatternReport);
    }

    /**
     * Get all FoundPatternReports.
     * @return ArrayList foundPatternReports
     */
    public List<FoundPatternReport> getFoundPatternReports() {
        return foundPatternReports;
    }

    /**
     * Return the complete report.
     * @return String report
     */
    public String getReport() {
        String returnString = "";

        for (FoundPatternReport foundPatternReport : this.foundPatternReports) {
            returnString += "\n\r" + foundPatternReport.getReport();
        }

        return returnString;
    }

    /**
     * Are any patterns found.
     * @return boolean for found or not
     */
    public boolean anyPatterns() {
        return this.foundPatternReports.size() > 0;
    }
}
