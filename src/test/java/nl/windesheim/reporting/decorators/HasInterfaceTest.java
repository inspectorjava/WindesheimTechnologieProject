package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HasInterfaceTest {

    private HasInterface hasInterface;

    private String interfaceName;

    @Before
    public void setUp() throws Exception {
        this.hasInterface = new HasInterface(new FoundPatternReport());
        this.interfaceName = "Interface name";
        this.hasInterface.setInterfaceName(this.interfaceName);
    }

    @Test
    public void getReport() {
        String report = this.hasInterface.getReport();
        assertEquals("Pattern: NONE found with certainty: NOT and uses interface: Interface name", report);
    }

    @Test
    public void buildTreeReport() {
        TreeBuilder builder = this.hasInterface.buildTreeReport(new TreeBuilder());
        TreeNode node = builder.build();
        assertEquals("Pattern: NONE", node.toString());
        assertEquals("Interface: " + this.interfaceName, node.getFirstChild().toString());
    }
}