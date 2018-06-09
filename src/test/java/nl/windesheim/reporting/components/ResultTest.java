package nl.windesheim.reporting.components;

import junit.framework.TestCase;

public class ResultTest extends TestCase {

    public void testResultCreation() {
        Result result = new Result();
        result.setCertainty(Result.Certainty.CERTAIN);
        assertEquals("CERTAIN", result.toString());

        Result resultUnlikely = new Result();
        resultUnlikely.setCertainty(Result.Certainty.UNLIKELY);
        assertEquals("UNLIKELY", resultUnlikely.toString());

        Result resultLikely = new Result();
        resultLikely.setCertainty(Result.Certainty.LIKELY);
        assertEquals("LIKELY", resultLikely.toString());
    }
}