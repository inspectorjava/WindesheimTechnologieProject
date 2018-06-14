package nl.windesheim.codeparser.analyzers.abstractfactory;

import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.patterns.AbstractFactory;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.reporting.Report;
import nl.windesheim.reporting.builders.AbstractFactoryFoundPatternBuilder;
import nl.windesheim.reporting.builders.CodeReportBuilder;
import nl.windesheim.reporting.components.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    public void abstractFactoryDemoTest() throws Exception {
        TestSettings settings = new TestSettings();

        File dir = new File(classLoader.getResource("abstractFactory/hjmf1954").getPath());
        settings.codeDir = dir;

        settings.commonParentName = "App";
        settings.commonParentFile = new File(classLoader.getResource("abstractFactory/hjmf1954/AbstractFactoryDemo.java").getPath());

        List<IDesignPattern> patterns = this.analyzeDirectory(settings.codeDir);

        assertEquals(patterns.size(), 1);
    }

    @Test
    public void abstractFactoryKingdomTest() throws Exception {
        TestSettings settings = new TestSettings();

        File dir = new File(classLoader.getResource("abstractFactory/iluwatar").getPath());
        settings.codeDir = dir;

        settings.commonParentName = "App";
        settings.commonParentFile = new File(classLoader.getResource("abstractFactory/iluwatar/App.java").getPath());

        List<IDesignPattern> patterns = this.analyzeDirectory(settings.codeDir);

        assertEquals(patterns.size(), 1);

        for (IDesignPattern pattern : patterns) {
            if(!(pattern instanceof AbstractFactory)) {
                continue;
            }

            CodeReportBuilder codeReportBuilder = Report.create();
            codeReportBuilder.addFoundPatternBuilder(Report.getMapper().getBuilder(pattern));
            CodeReport codeReport = codeReportBuilder.buildReport();
            TreePresentation tree = codeReport.getTreePresentation();
            assertNotNull(tree.getRoot());
        }
    }

    @Test
    public void abstractFactoryKingdomInvalidTest() throws Exception {
        TestSettings settings = new TestSettings();

        File dir = new File(classLoader.getResource("abstractFactory/iluwatar/invalid").getPath());
        settings.codeDir = dir;

        settings.commonParentName = "App";
        settings.commonParentFile = new File(classLoader.getResource("abstractFactory/iluwatar/invalid/App.java").getPath());

        List<IDesignPattern> patterns = this.analyzeDirectory(settings.codeDir);

        assertEquals(patterns.size(), 0);
    }

    private List<IDesignPattern> analyzeDirectory(File dir) throws IOException {
        Path directoryPath = dir.toPath();

        FileAnalysisProvider provider = new FileAnalysisProvider(new AbstractFactoryAnalyzer());

        return provider.analyzeDirectory(directoryPath);
    }

    class TestSettings {
        File codeDir;
        String commonParentName;
        File commonParentFile;
        HashMap<String, File> links = new HashMap<>();
    }
}
