package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HasInterfaceTest {

    private HasInterface hasInterface;

    private ClassOrInterface interfaceInstance;

    @Before
    public void setUp() throws Exception {
        this.hasInterface = new HasInterface(new FoundPatternReport());
        this.interfaceInstance = new ClassOrInterface().setName("INTERFACE name");
        this.hasInterface.setInterfaceName(this.interfaceInstance);
    }

    @Test
    public void getReport() {
        String report = this.hasInterface.getReport();
        assertEquals("Pattern: NONE found with certainty: NOT and uses interface: INTERFACE name", report);
    }

    @Test
    public void buildTreeReport() {
        TreeBuilder builder = this.hasInterface.buildTreeReport(new TreeBuilder());
        TreeNode node = builder.build();
        assertEquals("Pattern: NONE", node.toString());
        assertEquals("INTERFACE: " + this.interfaceInstance.getName(), node.getFirstChild().toString());
    }
}