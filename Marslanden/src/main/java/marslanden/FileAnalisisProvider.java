package marslanden;

import marslanden.analizers.PatternAnalyzer;
import marslanden.patterns.IDesignPattern;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by caveman on 4/19/18.
 */
public class FileAnalisisProvider {
    private ArrayList<PatternAnalyzer> analyzers = new ArrayList<>();

    public ArrayList<PatternAnalyzer> getAnalyzers() {
        return analyzers;
    }

    public FileAnalisisProvider addAnalyzer(PatternAnalyzer analyzer) {
        this.analyzers.add(analyzer);
        return this;
    }

    public FileAnalisisProvider removeAnalyzer(PatternAnalyzer analyzer) {
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
