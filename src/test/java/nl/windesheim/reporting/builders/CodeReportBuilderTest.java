package nl.windesheim.reporting.builders;

import junit.framework.TestCase;
import nl.windesheim.reporting.components.CodeReport;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CodeReportBuilderTest extends TestCase {

    public void testCreation() {
        CodeReportBuilder codeReportBuilder = new CodeReportBuilder();

        codeReportBuilder.addFoundPatternBuilder(new SingletonFoundPatternBuilder("test.java"));

        CodeReport codeReport = codeReportBuilder.buildReport();

        assertTrue(codeReport.anyPatterns());
    }

    public void testTreeCreation() {
        CodeReportBuilder codeReportBuilder = new CodeReportBuilder();

        codeReportBuilder.addFoundPatternBuilder(new SingletonFoundPatternBuilder("test.java"));

        String interfaceName = "Interfacename";

        List<String> files = new ArrayList<>();
        files.add("File1");
        files.add("File2");

        List<String> stratagies = new ArrayList<>();
        stratagies.add("Strategy1");
        stratagies.add("Strategy2");

        String context = "Context";

        StrategyFoundPatternBuilder strategyFoundPatternBuilder = new StrategyFoundPatternBuilder(files, context, interfaceName, stratagies);

        codeReportBuilder.addFoundPatternBuilder(strategyFoundPatternBuilder);

        CodeReport codeReport = codeReportBuilder.buildReport();

        System.out.println(codeReport.getTreePresentation());
    }

}