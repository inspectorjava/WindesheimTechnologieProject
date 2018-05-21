package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;
import nl.windesheim.reporting.components.TreePresentation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HasContextTest {

    private HasContext contextDecorator;

    private String contextName;

    @Before
    public void setUp() {
        this.contextName = "SetContextDecorator";
        this.contextDecorator = new HasContext(new FoundPatternReport());
        this.contextDecorator.setContext(this.contextName);
    }

    @Test
    public void getReport() {
        String report = this.contextDecorator.getReport();
        assertEquals("Pattern: NONE found with certainty: NOT - Context: " + this.contextName + "\n\r", report);
    }

    @Test
    public void buildTreeReport() {
        TreeBuilder builder = this.contextDecorator.buildTreeReport(new TreeBuilder());
        TreeNode node = builder.build();
        assertEquals("Context: " + this.contextName, builder.build().getFirstChild().toString());
    }
}