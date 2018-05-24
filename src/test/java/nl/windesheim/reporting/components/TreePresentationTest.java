package nl.windesheim.reporting.components;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreePresentationTest {

    @Test
    public void setRoot() {
        TreePresentation presentation = new TreePresentation();
        assertNull(presentation.getRoot());

        TreeNode node = new TreeNode("Test");
        presentation.setRoot(node);
        assertEquals(node, presentation.getRoot());
    }

    @Test
    public void addNode() {
        TreePresentation presentation = new TreePresentation();
        assertNull(presentation.getRoot());

        TreeNode node = new TreeNode("Test");
        presentation.setRoot(node);
        assertEquals(node, presentation.getRoot());
        TreeNode anotherNode = new TreeNode("test2");
        presentation.addNode(anotherNode);

        assertEquals(anotherNode, node.getFirstChild());
        assertEquals(anotherNode, presentation.getRoot().getFirstChild());
    }
}