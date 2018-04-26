package nl.windesheim.codeparser.marslanden;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import nl.windesheim.codeparser.marslanden.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.marslanden.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.marslanden.analyzers.singleton.SingletonAnalyzer;
import nl.windesheim.codeparser.marslanden.patterns.IDesignPattern;

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
     * The patter analyzer composite which will be called in the analyzeFile and analyzeDirectory functions
     */
    private PatternAnalyzer analyzer;

    public FileAnalysisProvider(PatternAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public ArrayList<IDesignPattern> analyzeFile(final URL file) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file.getFile());
        CompilationUnit cu = JavaParser.parse(fileInputStream);

        ArrayList<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();
        compilationUnits.add(cu);

        ArrayList<IDesignPattern> patterns = analyzer.analyze(compilationUnits);
        return patterns;
    }

    public ArrayList<IDesignPattern> analyzeDirectory(  Path directoryPath) throws IOException {
        SourceRoot sourceRoot = new SourceRoot(directoryPath);

        sourceRoot.tryToParse();

        ArrayList<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();
        compilationUnits.addAll(sourceRoot.getCompilationUnits());

        ArrayList<IDesignPattern> patterns = analyzer.analyze(compilationUnits);
        return patterns;
    }

    public PatternAnalyzer getAnalyzer() {
        return analyzer;
    }

    public FileAnalysisProvider setAnalyzer(PatternAnalyzer analyzer) {
        this.analyzer = analyzer;
        return this;
    }

    public static FileAnalysisProvider getConfiguredFileAnalysisProvider(){
        PatternAnalyzer analyzer = new PatternAnalyzerComposite();
        analyzer.addChild(new SingletonAnalyzer());

        FileAnalysisProvider analysisProvider = new FileAnalysisProvider(analyzer);
        return analysisProvider;
    }
}
