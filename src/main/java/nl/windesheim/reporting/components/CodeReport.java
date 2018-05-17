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
    private final List<IFoundPatternReport> foundReports;

    /**
     * Instantiate the foundReports ArrayList.
     */
    public CodeReport() {
        this.foundReports = new ArrayList<IFoundPatternReport>();
    }

    /**
     * Add a FoundPatternReport to the CodeReport.
     * @param foundReport the report
     */
    public void addFoundPatternReport(final IFoundPatternReport foundReport) {
        this.foundReports.add(foundReport);
    }

    /**
     * Get all FoundPatternReports.
     * @return ArrayList foundReports
     */
    public List<IFoundPatternReport> getFoundReports() {
        return foundReports;
    }

    /**
     * Return the complete report.
     * @return String report
     */
    public String getReport() {
        StringBuffer returnString = new StringBuffer();

        for (IFoundPatternReport foundReport : this.foundReports) {
            returnString.append("\n\r");
            returnString.append(foundReport.getReport());
        }

        return returnString.toString();
    }

    /**
     * @return TreeBuilder
     */
    public TreePresentation getTreePresentation() {
        TreePresentation presentation = new TreePresentation();

        presentation.setRoot(new TreeNode("Result"));
        for (IFoundPatternReport foundReport : this.foundReports) {
            TreeNode node = foundReport.buildTreeReport(new TreeBuilder()).build();
            presentation.addNode(node);
        }

        return presentation;
    }

    /**
     * Are any patterns found.
     * @return boolean for found or not
     */
    public boolean anyPatterns() {
        return this.foundReports.size() > 0;
    }
}
