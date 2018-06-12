package nl.windesheim.codeparser.analyzers.observer;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.ObserverPattern;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

class ObserverAnalyzerTestHelper {
    private FileAnalysisProvider provider;

    ObserverAnalyzerTestHelper () {
        provider = new FileAnalysisProvider(new ObserverAnalyzer());
    }

    List<IDesignPattern> analyzeDirectory (File dir) throws IOException {
        Path directoryPath = dir.toPath();
        return provider.analyzeDirectory(directoryPath);
    }

    void assertValidPattern (TestSettings settings) throws IOException {
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
}

class TestSettings {
    File codeDir;

    String abstractObservableName;
    File abstractObservableFile;

    String abstractObserverName;
    File abstractObserverFile;

    HashMap<String, File> concreteObservables = new HashMap<>();
    HashMap<String, File> concreteObservers = new HashMap<>();
}
