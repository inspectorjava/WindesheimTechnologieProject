package nl.windesheim.codeparser.analyzers.strategy;

import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Strategy;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StrategyAnalyzerEdgeCases {

    private ClassLoader classLoader;
    private StrategyAnalyzerTestHelper helper;

    public StrategyAnalyzerEdgeCases(){
        classLoader = this.getClass().getClassLoader();
        helper = new StrategyAnalyzerTestHelper();
    }

    @Test
    public void testMissingStrategies() throws IOException {
        File codeDir = new File(classLoader.getResource("strategy/missingStrategiesEdgeCase").getPath());

        //We shouldn't find any patterns
        Assert.assertEquals(0, helper.analyzeDirectory(codeDir).size());
    }

    @Test
    public void strategyNeverCalled() throws IOException {
        File codeDir = new File(classLoader.getResource("strategy/strategyNeverCalledEdgeCase").getPath());

        List<IDesignPattern> patternList = helper.analyzeDirectory(codeDir);

        //We shouldn't find any patterns
        Assert.assertEquals(1, patternList.size());

        Strategy pattern = (Strategy) patternList.get(0);

        Assert.assertFalse(pattern.isContextHasCaller());
        Assert.assertTrue(pattern.isContextHasSetter());
        Assert.assertTrue(pattern.isStrategyMethods());
    }

    @Test
    public void strategyInterfaceWithoutMethods() throws IOException {
        File codeDir = new File(classLoader.getResource("strategy/strategyInterfaceWithoutMethodsEdgeCase").getPath());

        List<IDesignPattern> patternList = helper.analyzeDirectory(codeDir);

        //We shouldn't find any patterns
        Assert.assertEquals(1, patternList.size());

        Strategy pattern = (Strategy) patternList.get(0);

        Assert.assertFalse(pattern.isContextHasCaller());
        Assert.assertTrue(pattern.isContextHasSetter());
        Assert.assertFalse(pattern.isStrategyMethods());
    }
}