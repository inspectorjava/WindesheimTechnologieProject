package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.ClassOrInterface;
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
     * file of file where pattern is found.
     */
    private final ClassOrInterface file;

    /**
     * Create the builder.
     * @param fileName filename of the file where singleton is found
     */
    public SingletonFoundPatternBuilder(final ClassOrInterface fileName) {
        super();
        this.file = fileName;
    }

    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.SINGLETON);
        HasSingleFile hasSingleFile = new HasSingleFile(patternReport);
        hasSingleFile.setFile(this.file);
        return hasSingleFile;
    }
}
