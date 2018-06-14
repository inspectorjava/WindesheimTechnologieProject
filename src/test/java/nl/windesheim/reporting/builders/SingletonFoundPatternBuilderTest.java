package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.FilePart;
import nl.windesheim.codeparser.patterns.Singleton;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.Result;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class SingletonFoundPatternBuilderTest {

    @Test
    public void buildReport() {
        FilePart part = new FilePart()
                .setFile(new File("SomeFileName.java"));

        SingletonFoundPatternBuilder singletonFoundPatternBuilder = new SingletonFoundPatternBuilder(new Singleton().setSingletonClass(new ClassOrInterface().setFilePart(part).setName("SingletonTest")));
        IFoundPatternReport foundPatternReport = singletonFoundPatternBuilder.buildReport();
        assertEquals( "Pattern: "+ DesignPatternType.SINGLETON +" found with certainty: "+ Result.Certainty.LIKELY +" with the following errors:\n" +
                " - The singleton has a non-private constructor\n" +
                " - Singleton: SingletonTest\n\r", foundPatternReport.getReport());
    }
}