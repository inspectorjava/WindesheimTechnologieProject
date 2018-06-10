package nl.windesheim.reporting.builders;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.patterns.ChainOfResponsibility;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.Result;
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
        ChainOfResponsibilityFoundPatternBuilder chainOfResponsibilityFoundPatternBuilder
            = new ChainOfResponsibilityFoundPatternBuilder(
                new ChainOfResponsibility()
                    .setChainLinks(links)
                    .setCommonParent(commonParent)
        );

        IFoundPatternReport report = chainOfResponsibilityFoundPatternBuilder.buildReport();

        assertEquals("Pattern: "+ DesignPatternType.CHAIN_OF_RESPONSIBILITY +" found with certainty: "+ Result.Certainty.LIKELY +" with the following errors: \n" +
                " - Common parent has no methods defined\n" +
                " and uses common parent: CommonParentTestLinks:\n\r- Link1\n\r" +
                "- Link2\n\r" +
                "- Link3\n\r", report.getReport());
    }
}