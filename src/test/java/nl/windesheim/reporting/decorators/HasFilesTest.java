package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.Result;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HasFilesTest {

    private HasFiles hasFilesDecorator;

    private List<String> files;

    @Before
    public void setUp() {
        this.files = new ArrayList<>();
        this.files.add("File1");
        this.files.add("File2");
        this.hasFilesDecorator = new HasFiles(new FoundPatternReport());
        this.hasFilesDecorator.setFiles(this.files);
    }

    @Test
    public void getReport() {
        String report = this.hasFilesDecorator.getReport();
        assertEquals("Pattern: " + DesignPatternType.NONE +" found with certainty: "+ Result.Certainty.CERTAIN +"File: File1\n\r" +
                "File: File2\n\r", report);
    }


    @Test
    public void buildTreeReport() {
        TreeBuilder builder = this.hasFilesDecorator.buildTreeReport(new TreeBuilder());
        TreeNode node = builder.build();
        TreeNode filesNode = node.getFirstChild();

        assertEquals("Files", filesNode.toString());

        TreeNode firstNode = filesNode.getFirstChild();
        assertEquals("File1", firstNode.toString());
        assertEquals("File2", firstNode.getNextSibling().toString());
    }
}