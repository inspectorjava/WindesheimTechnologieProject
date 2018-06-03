package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HasLinksTest {

    private ClassOrInterface link1;
    private ClassOrInterface link2;
    private ClassOrInterface link3;

    private HasLinks hasLinks;

    @Before
    public void setUp() throws Exception {
        this.hasLinks = new HasLinks(new FoundPatternReport());
        this.link1 = new ClassOrInterface().setName("Test1");
        this.link2 = new ClassOrInterface().setName("Test2");
        this.link3 = new ClassOrInterface().setName("Test3");

        List<ClassOrInterface> links = new ArrayList<>();

        links.add(this.link1);
        links.add(this.link2);
        links.add(this.link3);

        this.hasLinks.setLinks(links);
    }

    @Test
    public void getReport() {
        String report = this.hasLinks.getReport();
        assertEquals("Pattern: "+ DesignPatternType.NONE +" found with certainty: NOTLink: Test1\n\r" +
                "Link: Test2\n\r" +
                "Link: Test3\n\r", report);
    }

    @Test
    public void buildTreeReport() {
        TreeBuilder builder = this.hasLinks.buildTreeReport(new TreeBuilder());
        TreeNode node = builder.build();
        assertEquals("Pattern: " + DesignPatternType.NONE, node.toString());
        assertEquals("Links", node.getFirstChild().toString());
        assertEquals("Test1", node.getFirstChild().getFirstChild().toString());
        assertEquals("Test2", node.getFirstChild().getFirstChild().getNextSibling().toString());
        assertEquals("Test3", node.getFirstChild().getFirstChild().getNextSibling().getNextSibling().toString());
    }
}