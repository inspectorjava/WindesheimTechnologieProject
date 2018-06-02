package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.FilePart;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.FoundPatternReport;

import java.io.File;

import static org.junit.Assert.*;

public class HasSingleFileTest {

    @org.junit.Test
    public void testIfReturnStringIsCorrect() {
        FilePart part = new FilePart()
                .setFile(new File("SomeTestClass.java"));

        HasSingleFile hasSingleFile = new HasSingleFile(new FoundPatternReport());
        hasSingleFile.setFile(new ClassOrInterface().setFilePart(part));
        assertEquals( "Pattern: "+ DesignPatternType.NONE +" found with certainty: NOT\n\rFound in file: SomeTestClass.java",hasSingleFile.getReport());
    }
}