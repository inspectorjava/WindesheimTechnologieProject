package nl.windesheim.reporting.components;

import nl.windesheim.reporting.DesignPatternType;

import static org.junit.Assert.*;

public class FoundPatternReportTest {
    @org.junit.Test
    public void testIfDesignPatternTypeGetsSetProperly() {
        FoundPatternReport foundPatternReport = new FoundPatternReport();
        foundPatternReport.setDesignPatternType(DesignPatternType.SINGLETON);
        assertEquals(foundPatternReport.getDesignPatternType().toString(), DesignPatternType.SINGLETON.toString());
    }
}