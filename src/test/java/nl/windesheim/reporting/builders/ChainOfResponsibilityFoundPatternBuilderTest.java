package nl.windesheim.reporting.builders;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.IFoundPatternReport;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ChainOfResponsibilityFoundPatternBuilderTest {

    private List<ClassOrInterface> links;

    private ClassOrInterface commonParent;

    private ClassOrInterface link1 = new ClassOrInterface();
    private ClassOrInterface link2 = new ClassOrInterface();
    private ClassOrInterface link3 = new ClassOrInterface();

    @Before
    public void setUp() {
        this.links = new ArrayList<>();
        link1.setName("Link1");
        this.links.add(this.link1);

        link2.setName("Link2");
        this.links.add(this.link2);

        link3.setName("Link3");
        this.links.add(this.link3);

        this.commonParent = new ClassOrInterface().setName("CommonParentTest");

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