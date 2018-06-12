package nl.windesheim.reporting.components;

import java.util.ArrayList;
import java.util.List;

/**`
 * Build a tree.
 */
public class TreeBuilder {

    /**
     * The new tree.
     */
    private final TreePresentation tree;

    /**
     * Collect all nodes and add them later.
     */
    private final List<TreeNode> nodes;

    /**
     * Create a new TreePresentation.
     */
    public TreeBuilder() {
        tree = new TreePresentation();
        this.nodes = new ArrayList<>();
    }

    /**
     * Add a new node to the nodes list.
     * @param node TreeNode node
     */
    public void addNode(final TreeNode node) {
        this.nodes.add(node);
    }

    /**
     * Set the TreePresentation root.
     * @param node TreeNode node
     */
    public void setRoot(final TreeNode node) {
        this.tree.setRoot(node);
    }

    public TreeNode getRoot () {
        return this.tree.getRoot();
    }

    /**
     * Return the root node of the tree.
     * @return tree root
     */
    public TreeNode build() {
        for (TreeNode node : this.nodes) {
            this.tree.addNode(node);
        }

        return this.tree.getRoot();
    }
}
