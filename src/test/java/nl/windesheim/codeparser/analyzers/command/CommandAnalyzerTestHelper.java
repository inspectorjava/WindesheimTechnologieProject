package nl.windesheim.codeparser.analyzers.command;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.analyzers.strategy.StrategyAnalyzer;
import nl.windesheim.codeparser.patterns.Command;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Strategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

class CommandAnalyzerTestHelper {
    private ClassLoader classLoader;

    CommandAnalyzerTestHelper(){
        classLoader = this.getClass().getClassLoader();
    }

    List<IDesignPattern> analyzeDirectory(File dir) throws IOException{
        Path directoryPath = dir.toPath();

        SourceRoot sourceRoot = new SourceRoot(directoryPath);
        sourceRoot.tryToParse();

        PatternAnalyzerComposite analyzer = new PatternAnalyzerComposite();
        analyzer.addChild(new CommandAnalyzer());

        //The type solver can now solve types from the standard library and the code we are analyzing
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        combinedTypeSolver.add(new JavaParserTypeSolver(directoryPath));

        analyzer.setTypeSolver(combinedTypeSolver);

        ArrayList<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();
        compilationUnits.addAll(sourceRoot.getCompilationUnits());

        return analyzer.analyze(compilationUnits);
    }

     void testCommandPattern(TestSettings settings) throws IOException{
        List<IDesignPattern> patterns = analyzeDirectory(settings.codeDir);

        assertEquals(1, patterns.size());
        assertTrue(patterns.get(0) instanceof Command);

        Command pattern = (Command) patterns.get(0);

        //Assert that the command has values
        assertNotNull(pattern.getContext());
        assertNotNull(pattern.getCommandInterface());
        assertNotEquals(0, pattern.getCommands());

        //Check the context
        assertEquals(settings.contextClassName, pattern.getContext().getName());
        assertEquals(
                settings.contextfile,
                new File(settings.codeDir, pattern.getContext().getFilePart().getFile().getPath())
        );

        //Check the interface
        assertEquals(settings.interfaceName, pattern.getCommandInterface().getName());
        assertEquals(
                settings.interfaceFile,
                new File(settings.codeDir, pattern.getCommandInterface().getFilePart().getFile().getPath())
        );

        assertEquals(settings.commands.size(), pattern.getCommands().size());

        for (String strategyName : settings.commands.keySet()){
            File expectedFile = settings.commands.get(strategyName);
            boolean contains = false;

            for (ClassOrInterface classOrInterface : pattern.getCommands()){

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
    HashMap<String, File> commands = new HashMap<>();
}