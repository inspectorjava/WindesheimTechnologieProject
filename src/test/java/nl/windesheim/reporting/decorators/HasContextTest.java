package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HasContextTest {

    private HasContext contextDecorator;

    private ClassOrInterface context;

    @Before
    public void setUp() {
        this.context = new ClassOrInterface().setName("SetContextDecorator");
        this.contextDecorator = new HasContext(new FoundPatternReport());
        this.contextDecorator.setContext(this.context);
    }

    @Test
    public void getReport() {
        String report = this.contextDecorator.getReport();
        assertEquals("Pattern: NONE found with certainty: NOT - Context: " + this.context.getName() + "\n\r", report);
    }

    @Test
    public void buildTreeReport() {
        TreeBuilder builder = this.contextDecorator.buildTreeReport(new TreeBuilder());
        TreeNode node = builder.build();
        assertEquals("Context: " + this.context, builder.build().getFirstChild().toString());
    }
}