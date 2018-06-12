package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Singleton;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SingletonAnalyzerEdgeCases {

    private ClassLoader classLoader;

    public SingletonAnalyzerEdgeCases() {
        classLoader = this.getClass().getClassLoader();
    }

    @Test
    public void testFileForSingleton() throws Exception {
        URL filename = classLoader.getResource("singleton/IncompleteSingletons.java");

        SingletonAnalyzer analyzer = new SingletonAnalyzer();

        ArrayList<CompilationUnit> units = new ArrayList<>();
        units.add(JavaParser.parse(new File(filename.getFile())));

        List<IDesignPattern> patterns = analyzer.analyze(units);

        assertEquals(2, patterns.size());

        for (IDesignPattern pattern : patterns){
            assertFalse(((Singleton) pattern).hasPrivateConstructor());
        }
    }

}