package nl.windesheim.reporting.components;

public class TreePresentation {

    /**
     * Root TreeNode.
     */
    TreeNode root;

    /**
     * Set the root for the tree to this node.
     * @param node the new rootnode.
     */
    public void setRoot(final TreeNode node) {
        this.root = node;
    }

    /**
     * Add a new node to the tree.
     * @param node the new node to be added
     */
    public TreeNode getRoot() {
        return this.root;
    }

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
    private String printRow(TreeNode node) {
        StringBuilder returnString = new StringBuilder(node.toString());

        while(node.hasNextSibling()) {
            node = node.nextSibling;
            if (node.hasChildren()) {
                returnString.append("-").append(this.printRow(node.firstChild));
            }
            returnString.append(node.toString()).append("\n\r");
        }
        return returnString.toString();
    }
}
