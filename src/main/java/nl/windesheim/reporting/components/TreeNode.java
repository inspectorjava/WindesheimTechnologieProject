package nl.windesheim.reporting.components;

import nl.windesheim.codeparser.ClassOrInterface;

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
     * The class or the interface corresponding to this node.
     */
    private ClassOrInterface classOrInterface;

    /**
     * The type of the node.
     */
    private NodeType nodeType = NodeType.ROOT;

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
     * Construct with name and classOrInterface.
     * @param name name of the node
     * @param classOrInterface the class or interface of a node
     */
    public TreeNode(final String name, final ClassOrInterface classOrInterface) {
        this.name = name;
        this.classOrInterface = classOrInterface;
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

    /**
     * @return the class or interface of this node
     */
    public ClassOrInterface getClassOrInterface() {
        return classOrInterface;
    }

    /**
     * @param classOrInterface the class or interface to be set
     * @return this
     */
    public TreeNode setClassOrInterface(final ClassOrInterface classOrInterface) {
        this.classOrInterface = classOrInterface;
        return this;
    }

    /**
     * @return the type of the node
     */
    public NodeType getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType the type of the node
     * @return this
     */
    public TreeNode setNodeType(final NodeType nodeType) {
        this.nodeType = nodeType;
        return this;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
