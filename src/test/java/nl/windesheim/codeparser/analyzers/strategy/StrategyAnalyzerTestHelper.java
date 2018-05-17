package nl.windesheim.codeparser.analyzers.strategy;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Strategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

class StrategyAnalyzerTestHelper {
    private ClassLoader classLoader;

    StrategyAnalyzerTestHelper(){
        classLoader = this.getClass().getClassLoader();
    }

    List<IDesignPattern> analyzeDirectory(File dir) throws IOException{
        Path directoryPath = dir.toPath();

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

        return analyzer.analyze(compilationUnits);
    }

     void testStrategyPattern(TestSettings settings) throws IOException{
        List<IDesignPattern> patterns = analyzeDirectory(settings.codeDir);

        assertEquals(1, patterns.size());
        assertTrue(patterns.get(0) instanceof Strategy);

        Strategy pattern = (Strategy) patterns.get(0);

        //Assert that the strategy has values
        assertNotNull(pattern.getContext());
        assertNotNull(pattern.getStrategyInterface());
        assertNotEquals(0, pattern.getStrategies());

        //Check the context
        assertEquals(settings.contextClassName, pattern.getContext().getName());
        assertEquals(
                settings.contextfile,
                new File(settings.codeDir, pattern.getContext().getFilePart().getFile().getPath())
        );

        //Check the interface
        assertEquals(settings.interfaceName, pattern.getStrategyInterface().getName());
        assertEquals(
                settings.interfaceFile,
                new File(settings.codeDir, pattern.getStrategyInterface().getFilePart().getFile().getPath())
        );

        assertEquals(settings.strategies.size(), pattern.getStrategies().size());

        for (String strategyName : settings.strategies.keySet()){
            File expectedFile = settings.strategies.get(strategyName);
            boolean contains = false;

            for (ClassOrInterface classOrInterface : pattern.getStrategies()){

                File fullPathFile = new File(settings.codeDir, classOrInterface.getFilePart().getFile().getPath());
                if (classOrInterface.getName().equals(strategyName)){

                    //Check if the file is the same
                    assertEquals(expectedFile, fullPathFile);

                    //Mark the class as found
                    contains = true;
                    break;
                }
            }

            if (!contains){
                fail("Missing strategy class '" + strategyName + "' which was expected to be found in '" + expectedFile + "'");
            }
        }
    }
}

class TestSettings {
    File codeDir;
    String contextClassName;
    File contextfile;
    String interfaceName;
    File interfaceFile;
    HashMap<String, File> strategies = new HashMap<>();
}