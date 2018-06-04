package nl.windesheim.codeparser.analyzers.composite;

import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.patterns.CompositePattern;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class CompositeAnalyzerTest {

    private ClassLoader classLoader;

    @Before
    public void setUp() {
        this.classLoader = this.getClass().getClassLoader();
    }

    @Test
    public void testSimpleComposite() {
        try {

            File dir = new File(classLoader.getResource("composite/simpleComposite").getPath());
            Path directoryPath = dir.toPath();

            PatternAnalyzerComposite analyzer = new PatternAnalyzerComposite();
            analyzer.addChild(new CompositeAnalyzer());

            FileAnalysisProvider provider = new FileAnalysisProvider(analyzer);

            List<IDesignPattern> patterns = provider.analyzeDirectory(directoryPath);

            assertEquals(1, patterns.size());

            CompositePattern compositePattern = (CompositePattern) patterns.get(0);

            assertEquals(CompositePattern.class.getName(), compositePattern.getClass().getName());

            assertEquals("Employee", compositePattern.getComponent().getName());
            assertEquals("Manager", compositePattern.getComposites().get(0).getName());
            assertEquals("Developer", compositePattern.getLeafs().get(0).getName());

        } catch (IOException e) {
            fail("IO Exception" + e.getMessage());
        }
    }

    @Test
    public void testShapeComposite() {
        try {

            File dir = new File(classLoader.getResource("composite/shapeComposite").getPath());
            Path directoryPath = dir.toPath();

            PatternAnalyzerComposite analyzer = new PatternAnalyzerComposite();
            analyzer.addChild(new CompositeAnalyzer());

            FileAnalysisProvider provider = new FileAnalysisProvider(analyzer);

            List<IDesignPattern> patterns = provider.analyzeDirectory(directoryPath);

            assertEquals(1, patterns.size());

            CompositePattern compositePattern = (CompositePattern) patterns.get(0);

            assertEquals(CompositePattern.class.getName(), compositePattern.getClass().getName());

            assertEquals("Shape", compositePattern.getComponent().getName());
            assertEquals("Drawing", compositePattern.getComposites().get(0).getName());
            assertEquals("StrangeDrawing", compositePattern.getComposites().get(1).getName());
            assertEquals("Triangle", compositePattern.getLeafs().get(0).getName());
            assertEquals("Circle", compositePattern.getLeafs().get(1).getName());

        } catch (IOException e) {
            fail("IO Exception" + e.getMessage());
        }
    }

    @Test
    public void testNotAComposite() {
        try {

            File dir = new File(classLoader.getResource("composite/notAComposite").getPath());
            Path directoryPath = dir.toPath();

            PatternAnalyzerComposite analyzer = new PatternAnalyzerComposite();
            analyzer.addChild(new CompositeAnalyzer());

            FileAnalysisProvider provider = new FileAnalysisProvider(analyzer);

            List<IDesignPattern> patterns = provider.analyzeDirectory(directoryPath);

            assertEquals(0, patterns.size());
        } catch (IOException e) {
            fail("IO Exception" + e.getMessage());
        }
    }
}
