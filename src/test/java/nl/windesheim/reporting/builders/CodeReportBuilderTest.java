package nl.windesheim.reporting.builders;

import junit.framework.TestCase;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.patterns.Singleton;
import nl.windesheim.reporting.components.CodeReport;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CodeReportBuilderTest extends TestCase {

    public void testCreation() {
        CodeReportBuilder codeReportBuilder = new CodeReportBuilder();

        codeReportBuilder.addFoundPatternBuilder(new SingletonFoundPatternBuilder(
                new Singleton().setSingletonClass(
                        new ClassOrInterface().setName("test.java"))
                )
        );

        CodeReport codeReport = codeReportBuilder.buildReport();

        assertTrue(codeReport.anyPatterns());
    }
}