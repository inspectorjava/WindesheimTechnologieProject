package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.DesignPatternType;

import static org.junit.Assert.*;

public class HasSingleFileTest {

    @org.junit.Test
    public void testIfReturnStringIsCorrect() {
        HasSingleFile hasSingleFile = new HasSingleFile();
        hasSingleFile.setFileName("SomeTestClass.java");
        assertEquals( "Pattern: NONE found with certainty: NOT\n\rFound in file: SomeTestClass.java",hasSingleFile.getReport());
    }
}