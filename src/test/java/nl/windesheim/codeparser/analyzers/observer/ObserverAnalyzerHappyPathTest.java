package nl.windesheim.codeparser.analyzers.observer;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.PatternAnalyzerComposite;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.ObserverPattern;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class ObserverAnalyzerHappyPathTest {
    private ClassLoader classLoader;

    public ObserverAnalyzerHappyPathTest () {
        classLoader = this.getClass().getClassLoader();
    }

    @Test
    public void testNumberSystem () throws IOException {
        TestSettings settings = new TestSettings();

        try {
            settings.codeDir = new File(classLoader.getResource("observer/numbersystem").getPath());

            settings.abstractObservableName = "Subject";
            settings.abstractObservableFile = new File(
                    classLoader.getResource("observer/numbersystem/Subject.java").getPath()
            );

            settings.abstractObserverName = "Observer";
            settings.abstractObserverFile = new File(
                    classLoader.getResource("observer/numbersystem/Observer.java").getPath()
            );

            settings.concreteObservers.put(
                    "BinaryObserver",
                    new File(classLoader.getResource("observer/numbersystem/BinaryObserver.java").getPath())
            );
            settings.concreteObservers.put(
                    "HexaObserver",
                    new File(classLoader.getResource("observer/numbersystem/HexaObserver.java").getPath())
            );
            settings.concreteObservers.put(
                    "OctalObserver",
                    new File(classLoader.getResource("observer/numbersystem/OctalObserver.java").getPath())
            );

            assertValidPattern(settings);
        } catch (NullPointerException ex) {
            fail("Could not initialize test, test resources could not be loaded");
        }
    }

    @Test
    public void testObserverChat () throws IOException {
        TestSettings settings = new TestSettings();

        try {
            settings.codeDir = new File(classLoader.getResource("observer/observerChat").getPath());

            settings.abstractObservableName = "shared.MyObservable";
            settings.abstractObservableFile = new File(
                    classLoader.getResource("observer/observerChat/shared/MyObservable.java").getPath()
            );

            settings.abstractObserverName = "shared.MyObserver";
            settings.abstractObserverFile = new File(
                    classLoader.getResource("observer/observerChat/shared/MyObserver.java").getPath()
            );

            settings.concreteObservables.put(
                    "chat.Group",
                    new File(classLoader.getResource("observer/observerChat/chat/Group.java").getPath())
            );
            settings.concreteObservables.put(
                    "chat.User",
                    new File(classLoader.getResource("observer/observerChat/chat/User.java").getPath())
            );

            settings.concreteObservers.put(
                    "views.GroupBlock",
                    new File(classLoader.getResource("observer/observerChat/views/GroupBlock.java").getPath())
            );
            settings.concreteObservers.put(
                    "views.GroupContentPanel",
                    new File(classLoader.getResource("observer/observerChat/views/GroupContentPanel.java").getPath())
            );
            settings.concreteObservers.put(
                    "views.GroupOverviewPanel",
                    new File(classLoader.getResource("observer/observerChat/views/GroupOverviewPanel.java").getPath())
            );

            assertValidPattern(settings);
        } catch (NullPointerException ex) {
            fail("Could not initialize test, test resources could not be loaded");
        }
    }

    private void assertValidPattern (TestSettings settings) throws IOException {
        List<IDesignPattern> patterns = analyzeDirectory(settings.codeDir);

        // Check if the expected amount of patterns is found
        assertEquals(1, patterns.size());

        // Check if we've found instances of an ObserverPattern
        assertTrue(patterns.get(0) instanceof ObserverPattern);

        ObserverPattern observerPattern = (ObserverPattern) patterns.get(0);

        // The pattern should at least contain an abstract observable and abstract observer
        assertNotNull(observerPattern.getAbstractObservable());
        assertNotNull(observerPattern.getAbstractObserver());

        // Check of de abstract observable overeenkomt met wat we verwachten
        assertEquals(settings.abstractObservableName, observerPattern.getAbstractObservable().getName());
        assertEquals(
                settings.abstractObservableFile,
                new File(observerPattern.getAbstractObservable().getFilePart().getFile().getPath())
        );

        // Check of de abstract observer overeenkomt met wat we verwachten
        assertEquals(settings.abstractObserverName, observerPattern.getAbstractObserver().getName());
        assertEquals(
                settings.abstractObserverFile,
                new File(observerPattern.getAbstractObserver().getFilePart().getFile().getPath())
        );

        // Check of de concrete observables overeenkomen met wat we verwachten
        assertClassOrInterfaceInCollection(observerPattern.getConcreteObservables(), settings.concreteObservables);

        // Check of de concrete observers overeenkomen met wat we verwachten
        assertClassOrInterfaceInCollection(observerPattern.getConcreteObservers(), settings.concreteObservers);
    }

    private void assertClassOrInterfaceInCollection (List<ClassOrInterface> resultData, Map<String, File> expectedData) {
        for (String expectedName : expectedData.keySet()){
            File expectedFile = expectedData.get(expectedName);
            boolean contains = false;

            for (ClassOrInterface classOrInterface : resultData) {
                File foundFile = new File(classOrInterface.getFilePart().getFile().getPath());
                if (classOrInterface.getName().equals(expectedName)) {
                    assertEquals(expectedFile, foundFile);
                }

                contains = true;
                break;
            }

            if (!contains){
                fail("Missing concrete observable class '" + expectedName + "' which was expected to be found in '" + expectedFile + "'");
            }
        }
    }

    private List<IDesignPattern> analyzeDirectory (File dir) throws IOException {
        Path directoryPath = dir.toPath();

        PatternAnalyzerComposite analyzer = new PatternAnalyzerComposite();
        analyzer.addChild(new ObserverAnalyzer());

        FileAnalysisProvider provider = new FileAnalysisProvider(analyzer);

        return provider.analyzeDirectory(directoryPath);
    }

    private class TestSettings {
        private File codeDir;

        private String abstractObservableName;
        private File abstractObservableFile;

        private String abstractObserverName;
        private File abstractObserverFile;

        private HashMap<String, File> concreteObservables = new HashMap<>();
        private HashMap<String, File> concreteObservers = new HashMap<>();
    }
}
