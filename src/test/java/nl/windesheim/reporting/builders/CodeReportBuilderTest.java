package nl.windesheim.reporting.builders;

import junit.framework.TestCase;
import nl.windesheim.reporting.components.CodeReport;

import static org.junit.Assert.*;

public class CodeReportBuilderTest extends TestCase {

    public void testCreation() {
        CodeReportBuilder codeReportBuilder = new CodeReportBuilder();

        codeReportBuilder.addFoundPatternBuilder(new SingletonFoundPatternBuilder("test.java"));

        CodeReport codeReport = codeReportBuilder.buildReport();

        assertTrue(codeReport.anyPatterns());
    }

}