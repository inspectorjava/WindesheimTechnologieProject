package nl.windesheim.reporting.components;

public class TreePresentation {

    TreeNode root;

    public TreePresentation() {

    }

    public void setRoot(final TreeNode node) {
        this.root = node;
    }

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
