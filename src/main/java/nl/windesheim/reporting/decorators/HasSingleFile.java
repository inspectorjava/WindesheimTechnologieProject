package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;

/**
 * Decorator for a report that has a single file.
 */
public class HasSingleFile extends FoundPatternReport implements IFoundPatternReport {

    /**
     * Filename of found pattern.
     */
    private String fileName;

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
        String returnString = "";
        returnString += super.getReport();
        returnString += "\n\r" + "Found in file: " + this.fileName;
        return returnString;
    }
}
