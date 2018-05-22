package nl.windesheim.reporting.builders;

import nl.windesheim.reporting.components.IFoundPatternReport;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ChainOfResponsibilityFoundPatternBuilderTest {

    private List<String> links;

    private String commonParent;

    private String link1 = "Link1";
    private String link2 = "Link2";
    private String link3 = "Link3";

    @Before
    public void setUp() {
        this.links = new ArrayList<>();
        this.links.add(this.link1);
        this.links.add(this.link2);
        this.links.add(this.link3);

        this.commonParent = "CommonParentTest";

    }

    @Test
    public void buildReport() {
        ChainOfResponsibilityFoundPatternBuilder chainOfResponsibilityFoundPatternBuilder = new ChainOfResponsibilityFoundPatternBuilder(this.commonParent, this.links);

        IFoundPatternReport report = chainOfResponsibilityFoundPatternBuilder.buildReport();

        assertEquals("Pattern: CHAIN_OF_RESPONSIBILITY found with certainty: NOT and uses interface: CommonParentTestLink: Link1\n\r" +
                "Link: Link2\n\r" +
                "Link: Link3\n\r", report.getReport());
    }
}