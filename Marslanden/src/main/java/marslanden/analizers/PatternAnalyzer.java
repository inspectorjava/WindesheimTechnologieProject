package marslanden.analizers;

import marslanden.patterns.IDesignPattern;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by caveman on 4/19/18.
 */
public abstract class PatternAnalyzer {
    public abstract ArrayList<IDesignPattern> analyseFile(File file);
    public abstract ArrayList<IDesignPattern> analyseDirectory(Path directoryPath);
}
