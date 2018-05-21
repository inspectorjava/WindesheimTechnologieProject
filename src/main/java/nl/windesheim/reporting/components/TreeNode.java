package nl.windesheim.reporting.components;

import java.util.List;

/**
 * TreeNode
 */
public class TreeNode {

    /**
     * Child nodes to be added.
     */
    TreeNode firstChild;

    /**
     *
     */
    TreeNode nextSibling;

    /**
     * The name of the node.
     */
    String name;

    /**
     * Default construct.
     */
    public TreeNode() {
        this.name = "";
    }

    /**
     * Construct with name.
     * @param name name of the node
     */
    public TreeNode(String name) {
        this.name = name;
    }

    /**
     * Add a child node.
     * @param node child node
     */
    public void addChild(TreeNode node) {
        if (this.firstChild == null) {
            this.firstChild = node;
            return;
        }
        this.setNextSibling(node);
    }

    /**
     * Add the node to the
     * @param node the new node
     */
    public void setNextSibling(TreeNode node) {
        if (this.hasNextSibling()) {
            this.nextSibling.setNextSibling(node);
            return;
        }

        this.nextSibling = node;
    }

    /**
     * Get the first child.
     * @return the first child
     */
    public TreeNode getFirstChild() {
        return firstChild;
    }

    /**
     * Get the first next sibling.
     * @return the first next sibling
     */
    public TreeNode getNextSibling() {
        return nextSibling;
    }

    public boolean hasNextSibling() {
        return this.nextSibling != null;
    }

    /**
     * Set the tree name.
     * @param name the name of the node.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Check if the node has children
     * @return boolean
     */
    public boolean hasChildren() {
        return this.firstChild != null;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
