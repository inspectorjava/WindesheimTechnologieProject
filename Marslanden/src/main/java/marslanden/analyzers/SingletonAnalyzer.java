package marslanden.analyzers;

import marslanden.patterns.IDesignPattern;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by caveman on 4/19/18.
 */
public class SingletonAnalyzer extends PatternAnalyzer{
    @Override
    public ArrayList<IDesignPattern> analyseFile(File file) {
        return null;
    }

    @Override
    public ArrayList<IDesignPattern> analyseDirectory(Path directoryPath) {
        return null;
    }
}
