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
    private ArrayList<PatternAnalyzer> analyzers = new ArrayList<>();

    public ArrayList<PatternAnalyzer> getAnalyzers() {
        return analyzers;
    }

    public FileAnalysisProvider addAnalyzer(PatternAnalyzer analyzer) {
        this.analyzers.add(analyzer);
        return this;
    }

    public FileAnalysisProvider removeAnalyzer(PatternAnalyzer analyzer) {
        this.analyzers.remove(analyzer);
        return this;
    }

    public ArrayList<IDesignPattern> analyzeFile(File file){
        throw new NotImplementedException();
     //   return new ArrayList<>();
    }


    public ArrayList<IDesignPattern> analyzeDirectory(  Path directoryPath){
        throw new NotImplementedException();
       // return new ArrayList<>();
    }
}
