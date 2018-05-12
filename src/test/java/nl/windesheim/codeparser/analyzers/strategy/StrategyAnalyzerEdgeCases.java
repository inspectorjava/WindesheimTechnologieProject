package nl.windesheim.codeparser.analyzers.strategy;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

        //We shouldn't find any patterns
        Assert.assertEquals(0, helper.analyzeDirectory(codeDir).size());
    }

    @Test
    public void strategyInterfaceWithoutMethods() throws IOException {
        File codeDir = new File(classLoader.getResource("strategy/strategyInterfaceWithoutMethodsEdgeCase").getPath());

        //We shouldn't find any patterns
        Assert.assertEquals(0, helper.analyzeDirectory(codeDir).size());
    }
}