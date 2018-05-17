package nl.windesheim.reporting.builders;

import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasSingleFile;

/**
 * Singleton pattern builder.
 */
public class SingletonFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * Filename of file where pattern is found.
     */
    private final String fileName;

    /**
     * Create the builder.
     * @param fileName filename of the file where singleton is found
     */
    public SingletonFoundPatternBuilder(final String fileName) {
        super();
        this.fileName = fileName;
    }

    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport foundPatternReport = new FoundPatternReport();
        foundPatternReport.setDesignPatternType(DesignPatternType.SINGLETON);
        HasSingleFile hasSingleFile = new HasSingleFile(foundPatternReport);
        hasSingleFile.setFileName(this.fileName);
        return hasSingleFile;
    }
}
