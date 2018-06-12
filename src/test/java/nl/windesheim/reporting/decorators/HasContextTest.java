package nl.windesheim.reporting.decorators;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.Result;
import nl.windesheim.reporting.components.TreeBuilder;
import nl.windesheim.reporting.components.TreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HasContextTest {

    private HasClassOrInterface contextDecorator;

    private ClassOrInterface context;

    @Before
    public void setUp() {
        this.context = new ClassOrInterface().setName("SetContextDecorator").setDeclaration(new ClassOrInterfaceDeclaration());
        this.contextDecorator = new HasClassOrInterface(new FoundPatternReport());
        this.contextDecorator.setName("Context");
        this.contextDecorator.setClassOrInterface(this.context);
    }

    @Test
    public void getReport() {
        String report = this.contextDecorator.getReport();
        assertEquals("Pattern: "+ DesignPatternType.NONE +" found with certainty: "+ Result.Certainty.CERTAIN +" - Context: " + this.context.getName() + "\n\r", report);
    }

    @Test
    public void buildTreeReport() {
        TreeBuilder builder = this.contextDecorator.buildTreeReport(new TreeBuilder());
        TreeNode node = builder.build();
        assertEquals("Context: " + this.context.getName(), builder.build().getFirstChild().toString());
    }
}