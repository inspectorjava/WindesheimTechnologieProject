package nl.windesheim.codeparser.analyzers.composite;

import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
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
    public void analyze() {
        try {
            this.test(new File(classLoader.getResource("composite/composite").getPath()));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private List<IDesignPattern> test(File dir) throws IOException {
        Path directoryPath = dir.toPath();

        PatternAnalyzerComposite analyzer = new PatternAnalyzerComposite();
        analyzer.addChild(new CompositeAnalyzer());

        FileAnalysisProvider provider = new FileAnalysisProvider(analyzer);

        return provider.analyzeDirectory(directoryPath);
    }
}
