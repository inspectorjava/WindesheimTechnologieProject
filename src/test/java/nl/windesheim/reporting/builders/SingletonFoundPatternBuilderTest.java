package nl.windesheim.reporting.builders;

import nl.windesheim.reporting.components.IFoundPatternReport;
import org.junit.Test;

import static org.junit.Assert.*;

public class SingletonFoundPatternBuilderTest {

    @Test
    public void buildReport() {
        SingletonFoundPatternBuilder singletonFoundPatternBuilder = new SingletonFoundPatternBuilder("SomeFileName.java");
        IFoundPatternReport foundPatternReport = singletonFoundPatternBuilder.buildReport();
        assertEquals( "Pattern: SINGLETON found with certainty: NOT\n\rFound in file: SomeFileName.java", foundPatternReport.getReport());
    }
}