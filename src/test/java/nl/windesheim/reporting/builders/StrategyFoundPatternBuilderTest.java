package nl.windesheim.reporting.builders;

import static org.junit.Assert.*;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.patterns.Strategy;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.Result;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class StrategyFoundPatternBuilderTest {

    @Test
    public void buildReport() {
        ClassOrInterface interfaceName = new ClassOrInterface().setName("Interfacename");

        List<ClassOrInterface> stratagies = new ArrayList<>();
        stratagies.add(new ClassOrInterface().setName("Strategy1"));
        stratagies.add(new ClassOrInterface().setName("Strategy2"));

        ClassOrInterface context = new ClassOrInterface().setName("Context");

        StrategyFoundPatternBuilder strategyFoundPatternBuilder = new StrategyFoundPatternBuilder(
                new Strategy()
                        .setContext(context)
                        .setStrategyInterface(interfaceName)
                        .setStrategies(stratagies)
        );

        IFoundPatternReport foundPatternReport = strategyFoundPatternBuilder.buildReport();

        assertEquals("Pattern: " + DesignPatternType.STRATEGY +" found with certainty: "+ Result.Certainty.UNLIKELY +" with the following errors:\n" +
                " - The context doesn't call the strategy\n" +
                " - The context has no setter for the strategy\n" +
                " - The strategy interface has no methods declared\n" +
                "Strategies:\n\r" +
                "- Strategy1\n\r" +
                "- Strategy2\n\r" +
                " - Context: Context\n\r" +
                " - Strategy interface: Interfacename\n\r", foundPatternReport.getReport());
    }
}