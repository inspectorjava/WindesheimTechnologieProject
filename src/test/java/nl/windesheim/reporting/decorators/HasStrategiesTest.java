package nl.windesheim.reporting.decorators;

import nl.windesheim.codeparser.ClassOrInterface;
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

public class HasStrategiesTest {

    private ClassOrInterface strategy1;
    private ClassOrInterface strategy2;
    private ClassOrInterface strategy3;

    private HasStrategies hasStrategies;

    @Before
    public void setUp() throws Exception {
        this.hasStrategies = new HasStrategies(new FoundPatternReport());
        this.strategy1 = new ClassOrInterface().setName("Test1");
        this.strategy2 = new ClassOrInterface().setName("Test2");
        this.strategy3 = new ClassOrInterface().setName("Test3");

        List<ClassOrInterface> strategies = new ArrayList<>();

        strategies.add(this.strategy1);
        strategies.add(this.strategy2);
        strategies.add(this.strategy3);

        this.hasStrategies.setStrategies(strategies);
    }

    @Test
    public void getReport() {
        String report = this.hasStrategies.getReport();
        assertEquals("Pattern: "+ DesignPatternType.NONE +" found with certainty: "+ Result.Certainty.CERTAIN +"Strategy: Test1\n\r" +
                "Strategy: Test2\n\r" +
                "Strategy: Test3\n\r", report);
    }

    @Test
    public void buildTreeReport() {
        TreeBuilder builder = this.hasStrategies.buildTreeReport(new TreeBuilder());
        TreeNode node = builder.build();
        assertEquals("Pattern: " + DesignPatternType.NONE, node.toString());
        assertEquals("Strategies", node.getFirstChild().toString());
        assertEquals("Test1", node.getFirstChild().getFirstChild().toString());
        assertEquals("Test2", node.getFirstChild().getFirstChild().getNextSibling().toString());
        assertEquals("Test3", node.getFirstChild().getFirstChild().getNextSibling().getNextSibling().toString());
    }
}