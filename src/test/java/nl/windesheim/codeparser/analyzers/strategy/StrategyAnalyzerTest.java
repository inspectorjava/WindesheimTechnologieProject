package nl.windesheim.codeparser.analyzers.strategy;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.analyzers.singleton.SingletonAnalyzer;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class StrategyAnalyzerTest {
    private ClassLoader classLoader;

    public StrategyAnalyzerTest(){
        classLoader = this.getClass().getClassLoader();
    }

    @Test
    public void testWikiStrategy() throws IOException {
        Path directoryPath = new File(classLoader.getResource("strategy/wiki").getPath()).toPath();

        SourceRoot sourceRoot = new SourceRoot(directoryPath);
        sourceRoot.tryToParse();

        PatternAnalyzerComposite analyzer = new PatternAnalyzerComposite();
        analyzer.addChild(new StrategyAnalyzer());

        //The type solver can now solve types from the standard library and the code we are analyzing
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        combinedTypeSolver.add(new JavaParserTypeSolver(directoryPath));

        analyzer.setTypeSolver(combinedTypeSolver);

        ArrayList<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();
        compilationUnits.addAll(sourceRoot.getCompilationUnits());

        ArrayList<IDesignPattern> patterns = analyzer.analyze(compilationUnits);

        System.out.println(patterns);
    }

}