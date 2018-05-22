package nl.windesheim.reporting.components;

/**
 * TreeNode.
 */
public class TreeNode {

    /**
     * Child nodes to be added.
     */
    private TreeNode firstChild;

    /**
     *
     */
    private TreeNode nextSibling;

    /**
     * The name of the node.
     */
    private String name;

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
    public TreeNode(final String name) {
        this.name = name;
    }

    /**
     * Add a child node.
     * @param node child node
     */
    public void addChild(final TreeNode node) {
        if (this.firstChild == null) {
            this.firstChild = node;
            return;
        }
        this.setNextSiblingForChild(node);
    }

    /**
     * Add the node to the.
     * @param node the new node
     */
    public void setNextSibling(final TreeNode node) {
        if (this.hasNextSibling()) {
            this.nextSibling.setNextSibling(node);
            return;
        }

        this.nextSibling = node;
    }

    /**
     * Set the next sibling of the first child.
     * @param node node
     */
    private void setNextSiblingForChild(final TreeNode node) {
        if (this.hasChildren()) {
            TreeNode child = this.firstChild;
            while (child.getNextSibling() != null) {
                child = child.getNextSibling();
            }

            child.setNextSibling(node);
        }
    }

    /**
     * Has this node a next sibling.
     * @return boolean
     */
    public boolean hasNextSibling() {
        return this.nextSibling != null;
    }

    /**
     * Set the tree name.
     * @param name the name of the node.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Check if the node has children.
     * @return boolean
     */
    public boolean hasChildren() {
        return this.firstChild != null;
    }

    /**
     * Return FirstChild node.
     * @return TreeNode
     */
    public TreeNode getFirstChild() {
        return this.firstChild;
    }

    /**
     * Return the next Sibling.
     * @return TreeNode
     */
    public TreeNode getNextSibling() {
        return this.nextSibling;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
