package marslanden;

import marslanden.analyzers.PatternAnalyzer;
import marslanden.patterns.IDesignPattern;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by caveman on 4/19/18.
 */
public class FileAnalysisProvider {
    private PatternAnalyzer analyzer;

    public FileAnalysisProvider(PatternAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public ArrayList<IDesignPattern> analyzeFile(File file){
        throw new NotImplementedException();
     //   return new ArrayList<>();
    }


    public ArrayList<IDesignPattern> analyzeDirectory(  Path directoryPath){
        throw new NotImplementedException();
       // return new ArrayList<>();
    }

    public PatternAnalyzer getAnalyzer() {
        return analyzer;
    }

    public FileAnalysisProvider setAnalyzer(PatternAnalyzer analyzer) {
        this.analyzer = analyzer;
        return this;
    }
}
