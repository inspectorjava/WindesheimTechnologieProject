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

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class SingletonAnalyzerHappyPathTest {

    @Parameterized.Parameters(name = "{0}")
    public static String[] data(){
        return new String[]{
        //        "singleton/BillPughSingleton.java",
                "singleton/EagerInitializedSingleton.java",
                "singleton/LazyInitializedSingleton.java",
        //        "singleton/SerializedSingleton.java",
                "singleton/StaticBlockSingleton.java",
                "singleton/ThreadSafeSingleton.java"
        };
    }

    private ClassLoader classLoader;
    private URL filename;

    public SingletonAnalyzerHappyPathTest(String filename) {
        classLoader = this.getClass().getClassLoader();
        this.filename = classLoader.getResource(filename);
    }

    @Test
    public void testFileForSingleton() throws Exception {
        SingletonAnalyzer analyzer = new SingletonAnalyzer();

        ArrayList<CompilationUnit> units = new ArrayList<>();
        units.add(JavaParser.parse(new File(filename.getFile())));

        ArrayList<IDesignPattern> patterns = analyzer.analyze(units);

        assertEquals(1, patterns.size());

        assertTrue(patterns.get(0) instanceof Singleton);

        Singleton singleton = (Singleton) patterns.get(0);

        assertNotNull(singleton.getClassPart().getFile());

        assertNotNull(singleton.getClassPart().getRange());
    }

}