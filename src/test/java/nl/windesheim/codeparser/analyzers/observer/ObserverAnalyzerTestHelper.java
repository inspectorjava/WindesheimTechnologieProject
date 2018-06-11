package nl.windesheim.codeparser.analyzers.observer;

import nl.windesheim.codeparser.FileAnalysisProvider;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

class ObserverAnalyzerTestHelper {
    private FileAnalysisProvider provider;

    ObserverAnalyzerTestHelper () {
        provider = new FileAnalysisProvider(new ObserverAnalyzer());
    }

    List<IDesignPattern> analyzeDirectory (File dir) throws IOException {
        Path directoryPath = dir.toPath();
        return provider.analyzeDirectory(directoryPath);
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
