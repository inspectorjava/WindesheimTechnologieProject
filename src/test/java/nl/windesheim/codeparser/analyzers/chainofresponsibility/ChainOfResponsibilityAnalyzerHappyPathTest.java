package nl.windesheim.codeparser.analyzers.chainofresponsibility;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.patterns.ChainOfResponsibility;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ChainOfResponsibilityAnalyzerHappyPathTest {

    private ClassLoader classLoader;

    public ChainOfResponsibilityAnalyzerHappyPathTest(){
        classLoader = this.getClass().getClassLoader();
    }

    private List<IDesignPattern> analyzeDirectory(File dir) throws IOException {
        Path directoryPath = dir.toPath();

        PatternAnalyzerComposite analyzer = new PatternAnalyzerComposite();
        analyzer.addChild(new ChainOfResponsibilityAnalyzer());

        FileAnalysisProvider provider = new FileAnalysisProvider(analyzer);

        return provider.analyzeDirectory(directoryPath);
    }

    private void assertValidPattern(TestSettings settings) throws IOException {
        List<IDesignPattern> patterns = analyzeDirectory(settings.codeDir);

        assertEquals(1, patterns.size());
        assertTrue(patterns.get(0) instanceof ChainOfResponsibility);

        ChainOfResponsibility pattern = (ChainOfResponsibility) patterns.get(0);

        //Assert that the chain of responsibility has values
        assertNotNull(pattern.getCommonParent());
        assertFalse(pattern.getChainLinks().isEmpty());

        //Check the common parent
        assertEquals(settings.commonParentName, pattern.getCommonParent().getName());
        assertEquals(
                settings.commonParentFile,
                new File(settings.codeDir, pattern.getCommonParent().getFilePart().getFile().getPath())
        );

        assertEquals(settings.links.size(), pattern.getChainLinks().size());

        for (String linkName : settings.links.keySet()){
            File expectedFile = settings.links.get(linkName);
            boolean contains = false;

            for (ClassOrInterface classOrInterface : pattern.getChainLinks()){

                File fullPathFile = new File(settings.codeDir, classOrInterface.getFilePart().getFile().getPath());
                if (classOrInterface.getName().equals(linkName)){

                    //Check if the file is the same
                    assertEquals(expectedFile, fullPathFile);

                    //Mark the class as found
                    contains = true;
                    break;
                }
            }

            if (!contains){
                fail("Missing chain class '" + linkName + "' which was expected to be found in '" + expectedFile + "'");
            }
        }
    }

    @Test
    public void atmDispenseTest() throws Exception {
        TestSettings settings = new TestSettings();

        File dir = new File(classLoader.getResource("chainOfResponsibility/atm").getPath());
        settings.codeDir = dir;

        settings.commonParentName = "DispenseChain";
        settings.commonParentFile = new File(classLoader.getResource("chainOfResponsibility/atm/DispenseChain.java").getPath());

        settings.links.put(
                "Dollar10Dispenser",
                new File(classLoader.getResource("chainOfResponsibility/atm/Dollar10Dispenser.java").getPath())
        );

        settings.links.put(
                "Dollar20Dispenser",
                new File(classLoader.getResource("chainOfResponsibility/atm/Dollar20Dispenser.java").getPath())
        );

        settings.links.put(
                "Dollar50Dispenser",
                new File(classLoader.getResource("chainOfResponsibility/atm/Dollar50Dispenser.java").getPath())
        );

        assertValidPattern(settings);
    }

    @Test
    public void wikiTest() throws Exception {
        TestSettings settings = new TestSettings();

        File dir = new File(classLoader.getResource("chainOfResponsibility/wiki").getPath());
        settings.codeDir = dir;

        settings.commonParentName = "PurchasePower";
        settings.commonParentFile = new File(classLoader.getResource("chainOfResponsibility/wiki/PurchasePower.java").getPath());

        settings.links.put(
                "ManagerPPower",
                new File(classLoader.getResource("chainOfResponsibility/wiki/Links.java").getPath())
        );

        settings.links.put(
                "DirectorPPower",
                new File(classLoader.getResource("chainOfResponsibility/wiki/Links.java").getPath())
        );

        settings.links.put(
                "VicePresidentPPower",
                new File(classLoader.getResource("chainOfResponsibility/wiki/Links.java").getPath())
        );

        settings.links.put(
                "PresidentPPower",
                new File(classLoader.getResource("chainOfResponsibility/wiki/Links.java").getPath())
        );

        assertValidPattern(settings);
    }

    class TestSettings {
        File codeDir;
        String commonParentName;
        File commonParentFile;
        HashMap<String, File> links = new HashMap<>();
    }
}