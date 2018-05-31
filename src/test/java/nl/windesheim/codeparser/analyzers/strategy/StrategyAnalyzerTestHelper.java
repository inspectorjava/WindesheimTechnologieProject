package nl.windesheim.codeparser.analyzers.strategy;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.analyzers.chainofresponsibility.ChainOfResponsibilityAnalyzer;
import nl.windesheim.codeparser.analyzers.singleton.SingletonAnalyzer;
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
    private FileAnalysisProvider provider;

    StrategyAnalyzerTestHelper(){
        classLoader = this.getClass().getClassLoader();

        PatternAnalyzerComposite composite = new PatternAnalyzerComposite();
        composite.addChild(new StrategyAnalyzer());

        provider = new FileAnalysisProvider(composite);
    }

    List<IDesignPattern> analyzeDirectory(File dir) throws IOException{
        Path directoryPath = dir.toPath();

        return provider.analyzeDirectory(directoryPath);
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
                new File(pattern.getContext().getFilePart().getFile().getPath())
        );

        //Check the interface
        assertEquals(settings.interfaceName, pattern.getStrategyInterface().getName());
        assertEquals(
                settings.interfaceFile,
                new File(pattern.getStrategyInterface().getFilePart().getFile().getPath())
        );

        assertEquals(settings.strategies.size(), pattern.getStrategies().size());

        for (String strategyName : settings.strategies.keySet()){
            File expectedFile = settings.strategies.get(strategyName);
            boolean contains = false;

            for (ClassOrInterface classOrInterface : pattern.getStrategies()){

                File fullPathFile = new File(classOrInterface.getFilePart().getFile().getPath());
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