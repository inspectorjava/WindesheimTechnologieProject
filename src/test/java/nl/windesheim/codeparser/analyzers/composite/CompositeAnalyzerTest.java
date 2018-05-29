package nl.windesheim.codeparser.analyzers.composite;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CompositeAnalyzerTest {

    private ClassLoader classLoader;

    @Before
    public void setUp() {
        this.classLoader = this.getClass().getClassLoader();
    }

    @Test
    public void analyze() {
        try {
            this.test(new File(classLoader.getResource("composite/composite").getPath()));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private List<IDesignPattern> test(File dir) throws IOException {
        Path directoryPath = dir.toPath();

        SourceRoot sourceRoot = new SourceRoot(directoryPath);
        sourceRoot.tryToParse();

        PatternAnalyzerComposite analyzer = new PatternAnalyzerComposite();
        analyzer.addChild(new CompositeAnalyzer());

        //The type solver can now solve types from the standard library and the code we are analyzing
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        combinedTypeSolver.add(new JavaParserTypeSolver(directoryPath));

        analyzer.setTypeSolver(combinedTypeSolver);

        ArrayList<CompilationUnit> compilationUnits = new ArrayList<>();
        compilationUnits.addAll(sourceRoot.getCompilationUnits());
        System.out.println(sourceRoot.getCompilationUnits());

        return analyzer.analyze(compilationUnits);
    }
}
