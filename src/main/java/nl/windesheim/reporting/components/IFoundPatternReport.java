package nl.windesheim.reporting.components;

/**
 * Found a pattern, report about it.
 */
public interface IFoundPatternReport {

    /**
     * Report back to the user.
     * @return String report string
     */
    String getReport();

    /**
     * Build the tree report
     * @return return a new node
     */
    TreeBuilder buildTreeReport(TreeBuilder builder);
}
