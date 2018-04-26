package nl.windesheim.codeparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.analyzers.singleton.SingletonAnalyzer;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by caveman on 4/19/18.
 */
public class FileAnalysisProvider {
    /**
     * The patter analyzer composite which will be called in the analyzeFile and analyzeDirectory functions.
     */
    private PatternAnalyzer analyzer;

    /**
     * @param analyzer the pattern analyzer which will be used to analyze files
     */
    public FileAnalysisProvider(final PatternAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * Analyzes a single file for design patterns.
     * @param file the file that should be analyzed
     * @return a list of patterns which were found
     * @throws FileNotFoundException if the file that was passed doesn't exist
     */
    public ArrayList<IDesignPattern> analyzeFile(final URL file) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file.getFile());
        CompilationUnit cu = JavaParser.parse(fileInputStream);

        ArrayList<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();
        compilationUnits.add(cu);

        ArrayList<IDesignPattern> patterns = analyzer.analyze(compilationUnits);
        return patterns;
    }

    /**
     * Analyzes a directory for design patterns.
     * @param directoryPath the path to analyze
     * @return a list of found patterns
     * @throws IOException if the directory doesn't exist
     */
    public ArrayList<IDesignPattern> analyzeDirectory(final Path directoryPath) throws IOException {
        SourceRoot sourceRoot = new SourceRoot(directoryPath);

        sourceRoot.tryToParse();

        ArrayList<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();
        compilationUnits.addAll(sourceRoot.getCompilationUnits());

        ArrayList<IDesignPattern> patterns = analyzer.analyze(compilationUnits);
        return patterns;
    }

    /**
     * @return the current analyzer that is used
     */
    public PatternAnalyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * @param analyzer the analyzer that will be used to analyze files
     * @return this
     */
    public FileAnalysisProvider setAnalyzer(final PatternAnalyzer analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    /**
     * @return a default preconfigured FileAnalysisProvider
     */
    public static FileAnalysisProvider getConfiguredFileAnalysisProvider() {
        PatternAnalyzerComposite composite = new PatternAnalyzerComposite();
        composite.addChild(new SingletonAnalyzer());

        FileAnalysisProvider analysisProvider = new FileAnalysisProvider(composite);
        return analysisProvider;
    }
}
