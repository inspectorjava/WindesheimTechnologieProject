package nl.windesheim.codeparser.analyzers.abstractfactory;

import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.analyzers.chainofresponsibility.ChainOfResponsibilityAnalyzer;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class AbstractFactoryAnalyzerHappyPathTest {

    private ClassLoader classLoader;

    public AbstractFactoryAnalyzerHappyPathTest(){
        classLoader = this.getClass().getClassLoader();
    }

    @Test
    public void abstractFactoryTest() throws Exception {
        TestSettings settings = new TestSettings();

        File dir = new File(classLoader.getResource("abstractFactory/iluwatar").getPath());
        settings.codeDir = dir;

        settings.commonParentName = "App";
        settings.commonParentFile = new File(classLoader.getResource("abstractfactory/iluwatar/App.java").getPath());

        settings.links.put(
                "Army",
                new File(classLoader.getResource("abstractFactory/iluwatar/Army.java").getPath())
        );
        settings.links.put(
                "Castle",
                new File(classLoader.getResource("abstractFactory/iluwatar/Castle.java").getPath())
        );
        settings.links.put(
                "ElfArmy",
                new File(classLoader.getResource("abstractFactory/iluwatar/ElfArmy.java").getPath())
        );
        settings.links.put(
                "ElfCastle",
                new File(classLoader.getResource("abstractFactory/iluwatar/ElfCastle.java").getPath())
        );
        settings.links.put(
                "ElfKing",
                new File(classLoader.getResource("abstractFactory/iluwatar/ElfKing.java").getPath())
        );
        settings.links.put(
                "ElfKingdomFactory",
                new File(classLoader.getResource("abstractFactory/iluwatar/ElfKingdomFactory.java").getPath())
        );
        settings.links.put(
                "King",
                new File(classLoader.getResource("abstractFactory/iluwatar/King.java").getPath())
        );
        settings.links.put(
                "KingdomFactory",
                new File(classLoader.getResource("abstractFactory/iluwatar/KingdomFactory.java").getPath())
        );
        settings.links.put(
                "OrcArmy",
                new File(classLoader.getResource("abstractFactory/iluwatar/OrcArmy.java").getPath())
        );
        settings.links.put(
                "OrcCastle",
                new File(classLoader.getResource("abstractFactory/iluwatar/OrcCastle.java").getPath())
        );
        settings.links.put(
                "OrcKing",
                new File(classLoader.getResource("abstractFactory/iluwatar/OrcKing.java").getPath())
        );
        settings.links.put(
                "OrcKingdomFactory",
                new File(classLoader.getResource("abstractFactory/iluwatar/OrcKingdomFactory.java").getPath())
        );

        List<IDesignPattern> patterns = this.analyzeDirectory(settings.codeDir);

        assertEquals(patterns.size(), 1);
    }

    private List<IDesignPattern> analyzeDirectory(File dir) throws IOException {
        Path directoryPath = dir.toPath();

        PatternAnalyzerComposite analyzer = new PatternAnalyzerComposite();
        analyzer.addChild(new AbstractFactoryAnalyzer());

        FileAnalysisProvider provider = new FileAnalysisProvider(analyzer);

        return provider.analyzeDirectory(directoryPath);
    }

    class TestSettings {
        File codeDir;
        String commonParentName;
        File commonParentFile;
        HashMap<String, File> links = new HashMap<>();
    }
}
