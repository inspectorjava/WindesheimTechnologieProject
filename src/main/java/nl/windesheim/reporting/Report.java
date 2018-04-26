package nl.windesheim.reporting;

import nl.windesheim.reporting.builders.CodeReportBuilder;

/**
 * Facade class.
 */
final class Report {

    /**
     * Private report.
     */
    private Report() { }
    /**
     * Create a new report using an static method.
     * @return CodeReportBuilder
     */
    public static CodeReportBuilder create() {
        return new CodeReportBuilder();
    }
}
