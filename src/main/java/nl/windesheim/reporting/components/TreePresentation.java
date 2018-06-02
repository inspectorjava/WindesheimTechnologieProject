package nl.windesheim.reporting.components;

/**
 * TreePresentation.
 */
public class TreePresentation {

    /**
     * ROOT TreeNode.
     */
    private TreeNode root;

    /**
     * Set the root for the tree to this node.
     * @param node the new rootnode.
     */
    public void setRoot(final TreeNode node) {
        this.root = node;
    }

    /**
     * Add a new node to the tree.
     * @return root TreeNode root
     */
    public TreeNode getRoot() {
        return this.root;
    }

    /**
     * Add node.
     * @param node node to add
     */
    public void addNode(final TreeNode node) {
        this.root.addChild(node);
    }

    @Override
    public String toString() {
        return printRow(this.root);
    }

    /**
     * Print single row.
     * @param node node to print.
     * @return string
     */
    private String printRow(final TreeNode node) {
        StringBuilder returnString = new StringBuilder(node.toString());

        TreeNode newNode = node;
        while (newNode.hasNextSibling()) {
            newNode = newNode.getNextSibling();
            if (newNode.hasChildren()) {
                returnString.append(" - ").append(this.printRow(newNode.getFirstChild()));
            }
            returnString.append(newNode.toString()).append("\n\r");
        }
        return returnString.toString();
    }
}
