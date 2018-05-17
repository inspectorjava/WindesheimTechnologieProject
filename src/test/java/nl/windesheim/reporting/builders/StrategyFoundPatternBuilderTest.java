package nl.windesheim.reporting.builders;

import static org.junit.Assert.*;

import nl.windesheim.reporting.components.IFoundPatternReport;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class StrategyFoundPatternBuilderTest {

    @Test
    public void buildReport() {
        String interfaceName = "Interfacename";

        List<String> files = new ArrayList<>();
        files.add("File1");
        files.add("File2");

        List<String> stratagies = new ArrayList<>();
        stratagies.add("Strategy1");
        stratagies.add("Strategy2");

        String context = "Context";

        StrategyFoundPatternBuilder strategyFoundPatternBuilder = new StrategyFoundPatternBuilder(files, context, interfaceName, stratagies);

        IFoundPatternReport foundPatternReport = strategyFoundPatternBuilder.buildReport();

        System.out.println(foundPatternReport.getReport());
    }
}