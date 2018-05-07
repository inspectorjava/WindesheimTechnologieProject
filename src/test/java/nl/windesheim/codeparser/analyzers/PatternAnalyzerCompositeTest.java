package nl.windesheim.codeparser.analyzers;

import nl.windesheim.codeparser.analyzers.singleton.SingletonAnalyzer;
import nl.windesheim.codeparser.patterns.Singleton;
import org.junit.Test;

import static org.junit.Assert.*;

public class PatternAnalyzerCompositeTest {
    @Test
    public void addChild() throws Exception {
        PatternAnalyzerComposite composite = new PatternAnalyzerComposite();
        composite.addChild(new SingletonAnalyzer());
        composite.addChild(new SingletonAnalyzer());

        assertEquals(2, composite.getChildren().size());
    }

    @Test
    public void removeChild() throws Exception {
        PatternAnalyzerComposite composite = new PatternAnalyzerComposite();

        SingletonAnalyzer singleton1 = new SingletonAnalyzer();
        SingletonAnalyzer singleton2 = new SingletonAnalyzer();
        SingletonAnalyzer singleton3 = new SingletonAnalyzer();

        composite.addChild(singleton1);
        composite.addChild(singleton2);
        composite.addChild(singleton3);

        composite.removeChild(singleton2);

        assertEquals(2, composite.getChildren().size());

        assertArrayEquals(new PatternAnalyzer[]{singleton1, singleton3}, composite.getChildren().toArray());
    }

}