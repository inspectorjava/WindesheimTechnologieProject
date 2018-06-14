package nl.windesheim.codeparser.analyzers.observer;

import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.ObserverPattern;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class ObserverAnalyzerNegativesTest {
    private ClassLoader classLoader;
    private ObserverAnalyzerTestHelper helper;

    public ObserverAnalyzerNegativesTest() {
        classLoader = this.getClass().getClassLoader();
        helper = new ObserverAnalyzerTestHelper();
    }

    @Test
    public void testCompositePattern () throws IOException {
        try {
            File codeDir = new File(classLoader.getResource("composite/shapeComposite").getPath());

            List<IDesignPattern> patterns = helper.analyzeDirectory(codeDir);

            // Check if the expected amount of patterns is found
            for (IDesignPattern pattern : patterns) {
                assertFalse(pattern instanceof ObserverPattern);
            }
        } catch (NullPointerException ex) {
            fail("Could not initialize test, test resources could not be loaded");
        }
    }
}
