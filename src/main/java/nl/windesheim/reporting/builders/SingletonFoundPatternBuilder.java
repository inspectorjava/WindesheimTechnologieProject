package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.patterns.Singleton;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.Result;
import nl.windesheim.reporting.decorators.HasClassOrInterface;

/**
 * Singleton pattern builder.
 */
public class SingletonFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * The pattern for which we want to build a report.
     */
    private final Singleton pattern;

    /**
     * Create the builder.
     *
     * @param pattern the pattern for which we want to build a report
     */
    public SingletonFoundPatternBuilder(final Singleton pattern) {
        super();
        this.pattern = pattern;
    }

    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.SINGLETON);

        if (!pattern.hasPrivateConstructor()) {
            patternReport.setCertainty(Result.Certainty.LIKELY);
            patternReport.addPatternError("The singleton has a non-private constructor");
        }

        HasClassOrInterface hasSingleFile = new HasClassOrInterface(patternReport);
        hasSingleFile.setName("Singleton");
        hasSingleFile.setClassOrInterface(this.pattern.getSingletonClass());
        return hasSingleFile;
    }
}
