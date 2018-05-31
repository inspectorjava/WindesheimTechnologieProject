package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.FilePart;
import nl.windesheim.reporting.components.IFoundPatternReport;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class SingletonFoundPatternBuilderTest {

    @Test
    public void buildReport() {
        FilePart part = new FilePart()
                .setFile(new File("SomeFileName.java"));

        SingletonFoundPatternBuilder singletonFoundPatternBuilder = new SingletonFoundPatternBuilder(new ClassOrInterface().setFilePart(part));
        IFoundPatternReport foundPatternReport = singletonFoundPatternBuilder.buildReport();
        assertEquals( "Pattern: SINGLETON found with certainty: NOT\n\rFound in file: SomeFileName.java", foundPatternReport.getReport());
    }
}