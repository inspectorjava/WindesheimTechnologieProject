package nl.windesheim.codeparser.analyzers.strategy;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class StrategyAnalyzerHappyPathTest {

    private ClassLoader classLoader;
    private StrategyAnalyzerTestHelper helper;

    public StrategyAnalyzerHappyPathTest(){
        classLoader = this.getClass().getClassLoader();
        helper = new StrategyAnalyzerTestHelper();
    }

    @Test
    public void testWikiStrategy() throws IOException {
        TestSettings settings = new TestSettings();

        settings.codeDir = new File(classLoader.getResource("strategy/wiki").getPath());

        settings.contextClassName = "Customer";
        settings.contextfile = new File(classLoader.getResource("strategy/wiki/Customer.java").getPath());

        settings.interfaceName = "BillingStrategy";
        settings.interfaceFile = new File(classLoader.getResource("strategy/wiki/BillingStrategy.java").getPath());

        settings.strategies.put(
                "NormalStrategy",
                new File(classLoader.getResource("strategy/wiki/NormalStrategy.java").getPath())
        );
        settings.strategies.put(
                "HappyHourStrategy",
                new File(classLoader.getResource("strategy/wiki/HappyHourStrategy.java").getPath())
        );

        helper.testStrategyPattern(settings);
    }

    @Test
    public void testCompressionStrategy() throws IOException {
        TestSettings settings = new TestSettings();
        settings.codeDir = new File(classLoader.getResource("strategy/compression").getPath());

        settings.contextClassName = "CompressionContext";
        settings.contextfile = new File(classLoader.getResource("strategy/compression/CompressionContext.java").getPath());

        settings.interfaceName = "CompressionStrategy";
        settings.interfaceFile = new File(classLoader.getResource("strategy/compression/CompressionStrategy.java").getPath());

        settings.strategies.put(
                "RarCompressionStrategy",
                new File(classLoader.getResource("strategy/compression/RarCompressionStrategy.java").getPath())
        );
        settings.strategies.put(
                "ZipCompressionStrategy",
                new File(classLoader.getResource("strategy/compression/ZipCompressionStrategy.java").getPath())
        );

        helper.testStrategyPattern(settings);
    }

}