package nl.windesheim.codeparser.analyzers.command;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.patterns.Command;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

class CommandAnalyzerTestHelper {
    private ClassLoader classLoader;

    CommandAnalyzerTestHelper() {
        classLoader = this.getClass().getClassLoader();
    }

    List<IDesignPattern> analyzeDirectory(File dir) throws IOException {
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

    /**
     * Test if the command patterns is valid.
     * @param settings Test settings.
     * @throws IOException Exception when file can't be opened.
     */
    void testCommandPattern(TestSettings settings) throws IOException {
        List<IDesignPattern> patterns = analyzeDirectory(settings.codeDir);

        assertEquals(1, patterns.size());
        assertTrue(patterns.get(0) instanceof Command);

        Command pattern = (Command) patterns.get(0);

        // Test the interface.
        assertEquals(settings.interfaceName, pattern.getCommandParent().getName());
        assertEquals(
                settings.interfaceFile,
                new File(settings.codeDir, pattern.getCommandParent().getFilePart().getFile().getPath())
        );

        // Test if all the commands are present.
        assertEquals(settings.commands.size(), pattern.getCommands().size());

        for (String commandName : settings.commands.keySet()) {
            File expectedFile = settings.commands.get(commandName);

            if (!checkIfClassExists(pattern, settings.codeDir, commandName, expectedFile)) {
                fail("Missing command class '" + commandName + "' which was expected to be found in '" + expectedFile + "'");
            }
        }

        // Test if all the receivers are present.
        assertEquals(settings.receivers.size(), pattern.getReceivers().size());

        for (String commandReceivers : settings.receivers.keySet()) {
            File expectedFile = settings.receivers.get(commandReceivers);

            if (!checkIfClassExists(pattern, settings.codeDir, commandReceivers, expectedFile)) {
                fail("Missing receiver class '" + commandReceivers + "' which was expected to be found in '" + expectedFile + "'");
            }
        }
    }

    /**
     * Test if the command patterns is invalid.
     * @param codeDir Code dir of the wrong command pattern.
     * @throws IOException Exception when file can't be opened.
     */
    void testInvalidCommandPattern(File codeDir) throws IOException {
        List<IDesignPattern> patterns = analyzeDirectory(codeDir);

        assertEquals(0, patterns.size());
    }

    /**
     * Check if the file exists in the files directory.
     * @param pattern Command pattern definition.
     * @param codeDir Code directory with the test resources.
     * @param name Name for the current class.
     * @param expectedFile Expected filename.
     * @return If the expected class exists.
     */
    protected boolean checkIfClassExists(Command pattern, File codeDir, String name, File expectedFile) {
        boolean contains = false;

        for (ClassOrInterface classOrInterface : pattern.getCommands()) {

            File fullPathFile = new File(codeDir, classOrInterface.getFilePart().getFile().getPath());
            if (classOrInterface.getName().equals(name)) {

                // Check if the file is the same.
                assertEquals(expectedFile, fullPathFile);

                // Mark the class as found.
                contains = true;
                break;
            }
        }

        return contains;
    }
}

class TestSettings {
    File codeDir;
    String interfaceName;
    File interfaceFile;
    HashMap<String, File> commands = new HashMap<>();
    HashMap<String, File> receivers = new HashMap<>();
}