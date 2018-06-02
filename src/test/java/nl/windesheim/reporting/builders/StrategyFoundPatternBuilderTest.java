package nl.windesheim.reporting.builders;

import static org.junit.Assert.*;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.IFoundPatternReport;
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

        StrategyFoundPatternBuilder strategyFoundPatternBuilder = new StrategyFoundPatternBuilder(context, interfaceName, stratagies);

        IFoundPatternReport foundPatternReport = strategyFoundPatternBuilder.buildReport();

        assertEquals("Pattern: " + DesignPatternType.STRATEGY +" found with certainty: NOTStrategy: Strategy1\n\r" +
                "Strategy: Strategy2\n\r" +
                " - Context: Context\n\r" +
                " and uses interface: Interfacename", foundPatternReport.getReport());
    }
}