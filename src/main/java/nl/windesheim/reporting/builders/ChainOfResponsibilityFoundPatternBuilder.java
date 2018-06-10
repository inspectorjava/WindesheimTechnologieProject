package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.patterns.ChainOfResponsibility;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.Result;
import nl.windesheim.reporting.decorators.HasClassList;
import nl.windesheim.reporting.decorators.HasCommonParent;

/**
 * Chain Of Responsibility pattern builder.
 */
public class ChainOfResponsibilityFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * The pattern for which we want to build a report.
     */
    private final ChainOfResponsibility pattern;

    /**
     * Create the builder.
     *
     * @param pattern the pattern for which to build a report
     */
    public ChainOfResponsibilityFoundPatternBuilder(
            final ChainOfResponsibility pattern
    ) {
        super();
        this.pattern = pattern;
    }

    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.CHAIN_OF_RESPONSIBILITY);

        int errors = 0;
        if (!pattern.parentHasMethods()) {
            errors++;
            patternReport.addPatternError("Common parent has no methods defined");
        }

        for (ClassOrInterface badLink : pattern.getNonChainedLinks()) {
            errors++;
            patternReport.addPatternError("Link '" + badLink.getName() + "' never calls the next link");
        }

        if (errors > 0) {
            patternReport.setCertainty(Result.Certainty.LIKELY);
        }

        if (errors > 1) {
            patternReport.setCertainty(Result.Certainty.UNLIKELY);
        }

        HasCommonParent hasCommonParent = new HasCommonParent(patternReport);
        hasCommonParent.setCommonParent(this.pattern.getCommonParent());

        HasClassList hasLinks = new HasClassList(hasCommonParent);
        hasLinks.setName("Links");
        hasLinks.setClasses(this.pattern.getChainLinks());

        return hasLinks;
    }
}
