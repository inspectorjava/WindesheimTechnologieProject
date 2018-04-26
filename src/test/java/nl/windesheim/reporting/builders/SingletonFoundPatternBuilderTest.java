package nl.windesheim.reporting.builders;

import nl.windesheim.reporting.components.FoundPatternReport;
import org.junit.Test;

import static org.junit.Assert.*;

public class SingletonFoundPatternBuilderTest {

    @Test
    public void buildReport() {
        SingletonFoundPatternBuilder singletonFoundPatternBuilder = new SingletonFoundPatternBuilder("SomeFileName.java");
        FoundPatternReport foundPatternReport = singletonFoundPatternBuilder.buildReport();
        assertEquals( "Pattern: SINGLETON found with certainty: NOT\n\rFound in file: SomeFileName.java", foundPatternReport.getReport());
    }
}