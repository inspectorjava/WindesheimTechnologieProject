package nl.windesheim.reporting.components;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreeNodeTest {

    @Test
    public void addChild() {
        TreeNode node = new TreeNode();
        assertNull(node.getFirstChild());
        assertNull(node.getNextSibling());

        TreeNode newNode = new TreeNode();
        node.addChild(newNode);
        assertEquals(newNode, node.getFirstChild());
        assertNull(node.getNextSibling());

        TreeNode anotherNode = new TreeNode();
        node.addChild(anotherNode);
        assertEquals(anotherNode, node.getNextSibling());

        TreeNode anotherAnotherNode = new TreeNode();
        node.addChild(anotherAnotherNode);
        assertEquals(anotherAnotherNode, node.getNextSibling().getNextSibling());
    }

    @Test
    public void setNextSibling() {
        TreeNode node = new TreeNode();
        assertNull(node.getNextSibling());
        TreeNode anotherNode = new TreeNode();
        node.setNextSibling(anotherNode);
        assertEquals(anotherNode, node.getNextSibling());
    }

    @Test
    public void hasNextSibling() {
        TreeNode node = new TreeNode();
        assertNull(node.getNextSibling());
        assertFalse(node.hasNextSibling());
        TreeNode anotherNode = new TreeNode();
        node.setNextSibling(anotherNode);
        assertTrue(node.hasNextSibling());
    }

    @Test
    public void setName() {
        TreeNode node = new TreeNode("Name");
        assertEquals("Name", node.toString());

        TreeNode newNode = new TreeNode();
        newNode.setName("Name2");
        assertEquals("Name2", newNode.toString());
    }

    @Test
    public void hasChildren() {
        TreeNode node = new TreeNode();
        assertNull(node.getNextSibling());
        assertFalse(node.hasChildren());
        TreeNode anotherNode = new TreeNode();
        node.addChild(anotherNode);
        assertTrue(node.hasChildren());
    }

    @Test
    public void getFirstChild() {
        TreeNode node = new TreeNode();
        assertNull(node.getFirstChild());
        TreeNode anotherNode = new TreeNode();
        node.addChild(anotherNode);
        assertEquals(anotherNode, node.getFirstChild());
    }

    @Test
    public void getNextSibling() {
        TreeNode node = new TreeNode();
        assertNull(node.getNextSibling());
        TreeNode anotherNode = new TreeNode();
        node.setNextSibling(anotherNode);
        assertEquals(anotherNode, node.getNextSibling());
    }
}