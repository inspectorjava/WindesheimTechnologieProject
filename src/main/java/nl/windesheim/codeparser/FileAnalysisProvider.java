package nl.windesheim.codeparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.analyzers.observer.ObserverAnalyzer;
import nl.windesheim.codeparser.analyzers.singleton.SingletonAnalyzer;
import nl.windesheim.codeparser.analyzers.strategy.StrategyAnalyzer;
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
     * The patter analyzer composite which will be called in the analyzeFile and analyzeDirectory functions.
     */
    private PatternAnalyzerComposite analyzer;

    /**
     * @param analyzer the pattern analyzer which will be used to analyze files
     */
    public FileAnalysisProvider(final PatternAnalyzerComposite analyzer) {
        this.analyzer = analyzer;
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

        analyzer.setTypeSolver(typeSolver);

        //The type solver can now solve types from the standard library and the code we are analyzing
        ParserConfiguration configuration = JavaParser.getStaticConfiguration().setSymbolResolver(new JavaSymbolSolver(typeSolver));
        JavaParser.setStaticConfiguration(configuration);

        CompilationUnit compilationUnit = JavaParser.parse(fileInputStream);

        ArrayList<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();
        compilationUnits.add(compilationUnit);

        return analyzer.analyze(compilationUnits);
    }

    /**
     * Analyzes a directory for design patterns.
     * @param directoryPath the path to analyze
     * @return a list of found patterns
     * @throws IOException if the directory doesn't exist
     */
    public List<IDesignPattern> analyzeDirectory(final Path directoryPath) throws IOException {
        //The type solver can now solve types from the standard library and the code we are analyzing
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        typeSolver.add(new JavaParserTypeSolver(directoryPath));

        SymbolResolver symbolResolver = new JavaSymbolSolver(typeSolver);
        ParserConfiguration configuration = JavaParser.getStaticConfiguration().setSymbolResolver(symbolResolver);
        JavaParser.setStaticConfiguration(configuration);

        SourceRoot sourceRoot = new SourceRoot(directoryPath);

        sourceRoot.tryToParse();

        analyzer.setTypeSolver(typeSolver);

        ArrayList<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();
        compilationUnits.addAll(sourceRoot.getCompilationUnits());

        return analyzer.analyze(compilationUnits);
    }

    /**
     * @return the current analyzer that is used
     */
    public PatternAnalyzerComposite getAnalyzer() {
        return analyzer;
    }

    /**
     * @param analyzer the analyzer that will be used to analyze files
     * @return this
     */
    public FileAnalysisProvider setAnalyzer(final PatternAnalyzerComposite analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    /**
     * @return a default preconfigured FileAnalysisProvider
     */
    public static FileAnalysisProvider getConfiguredFileAnalysisProvider() {
        PatternAnalyzerComposite composite = new PatternAnalyzerComposite();
//        composite.addChild(new SingletonAnalyzer());
        composite.addChild(new ObserverAnalyzer());
//        composite.addChild(new StrategyAnalyzer());

        return new FileAnalysisProvider(composite);
    }
}
