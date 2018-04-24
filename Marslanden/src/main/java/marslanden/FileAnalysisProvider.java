package marslanden;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import marslanden.analyzers.PatternAnalyzer;
import marslanden.analyzers.PatternAnalyzerComposite;
import marslanden.analyzers.singleton.SingletonAnalyzer;
import marslanden.patterns.IDesignPattern;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

        ArrayList<CompilationUnit> compilationUnits = new ArrayList<>();
        compilationUnits.add(cu);

        ArrayList<IDesignPattern> patterns = analyzer.analyze(compilationUnits);
        return patterns;
    }

    public ArrayList<IDesignPattern> analyzeDirectory(  Path directoryPath) throws IOException {
        SourceRoot sourceRoot = new SourceRoot(directoryPath);

        sourceRoot.tryToParse();

        ArrayList<CompilationUnit> compilationUnits = new ArrayList<>();
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
