package marslanden.analyzers.singleton;

import com.github.javaparser.ast.CompilationUnit;
import marslanden.analyzers.PatternAnalyzer;
import marslanden.patterns.IDesignPattern;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by caveman on 4/19/18.
 */
public class SingletonAnalyzer extends PatternAnalyzer {

    @Override
    protected ArrayList<IDesignPattern> analyzePattern(final ArrayList<CompilationUnit> files) {
        throw new NotImplementedException();
    //    return null;
    }
}
