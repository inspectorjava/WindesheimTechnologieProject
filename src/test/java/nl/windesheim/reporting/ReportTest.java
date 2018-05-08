package nl.windesheim.reporting;

import nl.windesheim.reporting.builders.CodeReportBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReportTest {

    @Test
    public void create() {
        assertEquals(CodeReportBuilder.class, Report.create().getClass());
    }
}