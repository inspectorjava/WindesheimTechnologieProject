package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Singleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SingletonAnalyzerEdgeCases {

    private ClassLoader cl;

    public SingletonAnalyzerEdgeCases() {
        cl = this.getClass().getClassLoader();
    }

    @Test
    public void testFileForSingleton() throws Exception {
        URL filename = cl.getResource("singleton/IncompleteSingletons.java");

        SingletonAnalyzer analyzer = new SingletonAnalyzer();

        ArrayList<CompilationUnit> units = new ArrayList<>();
        units.add(JavaParser.parse(new File(filename.getFile())));

        ArrayList<IDesignPattern> patterns = analyzer.analyzePattern(units);

        assertEquals(0, patterns.size());
    }

}