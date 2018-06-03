package nl.windesheim.reporting.builders;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
import nl.windesheim.codeparser.patterns.AbstractFactory;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasContext;
import nl.windesheim.reporting.decorators.HasFiles;

import java.util.ArrayList;
import java.util.List;

/**
 * The abstract factory found pattern builder.
 */
public class AbstractFactoryFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * The factory name to use.
     */
    private final ClassOrInterface factory;

    /**
     * List of the factory implementations.
     */
    private final List<ClassOrInterface> implementations;

    /**
     * The constructor.
     * @param factory The name for this factory.
     */
    public AbstractFactoryFoundPatternBuilder(final AbstractFactory factory) {
        super();

        this.factory = new ClassOrInterface()
                .setFilePart(FilePartResolver.getFilePartOfNode(factory.getFactoryInterface()))
                .setName(factory.getFactoryInterface().getNameAsString())
                .setDeclaration(factory.getFactoryInterface());

        this.implementations = new ArrayList<>();
        for (ClassOrInterfaceDeclaration declaration : factory.getImplementations()) {
            this.implementations.add(
                    new ClassOrInterface()
                    .setFilePart(FilePartResolver.getFilePartOfNode(declaration))
                    .setName(declaration.getNameAsString())
                    .setDeclaration(declaration)
            );
        }
    }

    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.ABSTRACT_FACTORY);
        HasContext hasContext = new HasContext(patternReport);
        hasContext.setContext(this.factory);

        HasFiles hasFiles = new HasFiles(hasContext);
        List<String> implementations = new ArrayList<>();
        for (ClassOrInterface classOrInterface : this.implementations) {
            implementations.add(classOrInterface.getName());
        }
        hasFiles.setFiles(implementations);

        return hasContext;
    }
}
