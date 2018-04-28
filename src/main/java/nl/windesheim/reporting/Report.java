package nl.windesheim.reporting;

import nl.windesheim.reporting.builders.CodeReportBuilder;

/**
 * Facade class.
 */
public final class Report {

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

    /**
     * Get the CodeReportDesignPatternMapper.
     * @return CodeReportDesignPatternMapper
     */
    public static CodeReportDesignPatternMapper getMapper() {
        return new CodeReportDesignPatternMapper();
    }
}
