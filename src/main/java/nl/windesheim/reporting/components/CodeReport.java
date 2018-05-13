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
    private final List<FoundPatternReport> foundReports;

    /**
     * Instantiate the foundReports ArrayList.
     */
    public CodeReport() {
        this.foundReports = new ArrayList<FoundPatternReport>();
    }

    /**
     * Add a FoundPatternReport to the CodeReport.
     * @param foundReport the report
     */
    public void addFoundPatternReport(final FoundPatternReport foundReport) {
        this.foundReports.add(foundReport);
    }

    /**
     * Get all FoundPatternReports.
     * @return ArrayList foundReports
     */
    public List<FoundPatternReport> getFoundReports() {
        return foundReports;
    }

    /**
     * Return the complete report.
     * @return String report
     */
    public String getReport() {
        StringBuffer returnString = new StringBuffer();

        for (FoundPatternReport foundReport : this.foundReports) {
            returnString.append("\n\r");
            returnString.append(foundReport.getReport());
        }

        return returnString.toString();
    }

    /**
     * Are any patterns found.
     * @return boolean for found or not
     */
    public boolean anyPatterns() {
        return this.foundReports.size() > 0;
    }
}
