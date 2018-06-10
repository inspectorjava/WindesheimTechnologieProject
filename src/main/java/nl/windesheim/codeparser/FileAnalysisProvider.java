package nl.windesheim.codeparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.abstractfactory.AbstractFactoryAnalyzer;
import nl.windesheim.codeparser.analyzers.chainofresponsibility.ChainOfResponsibilityAnalyzer;
import nl.windesheim.codeparser.analyzers.command.CommandAnalyzer;
import nl.windesheim.codeparser.analyzers.composite.CompositeAnalyzer;
import nl.windesheim.codeparser.analyzers.observer.ObserverAnalyzer;
import nl.windesheim.codeparser.analyzers.singleton.SingletonAnalyzer;
import nl.windesheim.codeparser.analyzers.strategy.StrategyAnalyzer;
import nl.windesheim.codeparser.analyzers.util.ErrorLog;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caveman on 4/19/18.
 */
public class FileAnalysisProvider {
    /**
     * The pattern analyzers, which will be called in the analyzeFile and analyzeDirectory functions.
     */
    private List<PatternAnalyzer> analyzers;

    /**
     * @param analyzer the pattern analyzer which will be used to analyze files
     */
    public FileAnalysisProvider(final PatternAnalyzer analyzer) {
        this.analyzers = new ArrayList<>();
        this.analyzers.add(analyzer);
    }

    public FileAnalysisProvider(final List<PatternAnalyzer> analyzers) {
        this.analyzers = analyzers;
    }

    /**
     * Analyzes a single file for design patterns.
     * @param fileName the file that should be analyzed
     * @return a list of patterns which were found
     * @throws FileNotFoundException if the file that was passed doesn't exist
     */
    public List<IDesignPattern> analyzeFile(final URL fileName) throws FileNotFoundException {
        File fileInputStream = new File(fileName.getFile());

        //The type solver can now solve types from the standard library and the code we are analyzing
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(new JavaParserTypeSolver(fileInputStream));

        //The type solver can now solve types from the standard library and the code we are analyzing
        ParserConfiguration configuration =
                JavaParser.getStaticConfiguration().setSymbolResolver(new JavaSymbolSolver(typeSolver));
        JavaParser.setStaticConfiguration(configuration);

        CompilationUnit compilationUnit = JavaParser.parse(fileInputStream);

        return runAnalysis(compilationUnit, typeSolver);
    }

    /**
     * Analyzes a directory for design patterns.
     * @param directoryPath the path to analyze
     * @return a list of found patterns
     * @throws IOException if the directory doesn't exist
     */
    public List<IDesignPattern> analyzeDirectory(final Path directoryPath) throws IOException {
        SourceRoot sourceRoot = new SourceRoot(directoryPath);

        //The type solver can now solve types from the standard library and the code we are analyzing
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(new JavaParserTypeSolver(directoryPath));

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);

        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setSymbolResolver(symbolSolver);

        sourceRoot.setParserConfiguration(configuration);

        sourceRoot.tryToParse();

        return runAnalysis(sourceRoot.getCompilationUnits(), typeSolver);
    }

    public FileAnalysisProvider addAnalyzer(final PatternAnalyzer analyzer) {
        analyzers.add(analyzer);
        return this;
    }

    public FileAnalysisProvider setAnalyzers(final List<PatternAnalyzer> analyzers) {
        this.analyzers = analyzers;
        return this;
    }

    private List<IDesignPattern> runAnalysis (final CompilationUnit compilationUnit, final TypeSolver typeSolver) {
        List<CompilationUnit> compilationUnits = new ArrayList<>();
        compilationUnits.add(compilationUnit);

        return runAnalysis(compilationUnits, typeSolver);
    }

    private List<IDesignPattern> runAnalysis (final List<CompilationUnit> compilationUnits, final TypeSolver typeSolver) {
        List<IDesignPattern> patterns = new ArrayList<>();

        for (PatternAnalyzer analyzer : analyzers) {
            analyzer.setTypeSolver(typeSolver);
            patterns.addAll(analyzer.analyze(compilationUnits));
        }

        return patterns;
    }

    /**
     * @return a list of errors encountered while analyzing
     */
    public List<Exception> getErrors() {
        return ErrorLog.getInstance().getErrors();
    }

    /**
     * @return a default preconfigured FileAnalysisProvider
     */
    public static FileAnalysisProvider getConfiguredFileAnalysisProvider() {
        List<PatternAnalyzer> analyzers = new ArrayList<>();

        analyzers.add(new SingletonAnalyzer());
        analyzers.add(new StrategyAnalyzer());
        analyzers.add(new ChainOfResponsibilityAnalyzer());
        analyzers.add(new ObserverAnalyzer());
        analyzers.add(new AbstractFactoryAnalyzer());
        analyzers.add(new CommandAnalyzer());
        analyzers.add(new CompositeAnalyzer());

        return new FileAnalysisProvider(analyzers);
    }
}
