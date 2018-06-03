package nl.windesheim.reporting.builders;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasContext;

/**
 * The abstract factory found pattern builder.
 */
public class AbstractFactoryFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * The factory name to use.
     */
    private final ClassOrInterface factory;

    /**
     * The constructor.
     * @param factoryName The name for this factory.
     */
    public AbstractFactoryFoundPatternBuilder(final ClassOrInterfaceDeclaration factory) {
        super();

        this.factory = new ClassOrInterface()
                .setFilePart(FilePartResolver.getFilePartOfNode(factory))
                .setName(factory.getNameAsString())
                .setDeclaration(factory);
    }

    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.ABSTRACT_FACTORY);
        HasContext hasContext = new HasContext(patternReport);
        hasContext.setContext(this.factory);
        return hasContext;
    }
}
