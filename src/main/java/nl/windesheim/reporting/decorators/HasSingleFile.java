package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.IFoundPatternReport;

/**
 * Decorator for a report that has a single file.
 */
public class HasSingleFile extends FoundPatternReportDecorator {

    /**
     * Filename of found pattern.
     */
    private String fileName;

    /**
     * Default constructor.
     * @param report for decorator
     */
    public HasSingleFile(final IFoundPatternReport report) {
        super(report);
    }

    /**
     * Set the filename.
     * @param fileName pattern was found in this file.
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     *
     * @return String report string.
     */
    @Override
    public String getReport() {
        return super.getReport() + "\n\r" + "Found in file: " + this.fileName;
    }


}
