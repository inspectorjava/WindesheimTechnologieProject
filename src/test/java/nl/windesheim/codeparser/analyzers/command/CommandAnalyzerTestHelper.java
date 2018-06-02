package nl.windesheim.codeparser.analyzers.command;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.patterns.Command;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

class CommandAnalyzerTestHelper {
    private FileAnalysisProvider provider;

    CommandAnalyzerTestHelper() {
        PatternAnalyzerComposite composite = new PatternAnalyzerComposite();
        composite.addChild(new CommandAnalyzer());

        provider = new FileAnalysisProvider(composite);
    }

    List<IDesignPattern> analyzeDirectory(File dir) throws IOException{
        Path directoryPath = dir.toPath();

        return provider.analyzeDirectory(directoryPath);
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

        File f = new File(pattern.getCommandParent().getFilePart().getFile().getPath());

        // Test the interface.
        assertEquals(settings.interfaceName, pattern.getCommandParent().getName());
        assertEquals(
                settings.interfaceFile,
                new File(pattern.getCommandParent().getFilePart().getFile().getPath())
        );

        // Test if all the commands are present.
        assertEquals(settings.commands.size(), pattern.getCommands().size());

        for (String commandName : settings.commands.keySet()) {
            File expectedFile = settings.commands.get(commandName);

            if (!checkIfClassExists(pattern.getCommands(), commandName, expectedFile)) {
                fail("Missing command class '" + commandName + "' which was expected to be found in '" + expectedFile + "'");
            }
        }

        // Test if all the receivers are present.
        assertEquals(settings.receivers.size(), pattern.getReceivers().size());

        for (String commandReceivers : settings.receivers.keySet()) {
            if (!checkIfClassExists(pattern.getReceivers(), commandReceivers)) {
                fail("Missing receiver class '" + commandReceivers + "'");
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
     * @param classes List of classes with the expected class.
     * @param name Name for the current class.
     * @param expectedFile Expected filename.
     * @return If the expected class exists.
     */
    protected boolean checkIfClassExists(List<ClassOrInterface> classes, String name, File expectedFile) {
        boolean contains = false;

        for (ClassOrInterface classOrInterface : classes) {

            File fullPathFile = new File(classOrInterface.getFilePart().getFile().getPath());
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

    /**
     * Check if the class exists.
     * @param classes List of classes with the expected class.
     * @param name Name for the current class.
     * @return If the expected class exists.
     */
    protected boolean checkIfClassExists(List<ClassOrInterface> classes, String name) {

        for (ClassOrInterface classOrInterface : classes) {
            if (classOrInterface.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }
}

class TestSettings {
    File codeDir;
    String interfaceName;
    File interfaceFile;
    HashMap<String, File> commands = new HashMap<>();
    HashMap<String, File> receivers = new HashMap<>();
}