package nl.windesheim.codeparser.analyzers.util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import nl.windesheim.codeparser.FilePart;

import java.io.File;
import java.util.ArrayList;

/**
 * Resolves FileParts.
 */
public final class FilePartResolver {

    /**
     *
     */
    private FilePartResolver() {

    }

    /**
     * @param node The node you want the FilePart of
     * @return the FilePart of the node
     */
    public static FilePart getFilePartOfNode(final Node node) {

        Node currentNode = node;
        while (!(currentNode instanceof CompilationUnit)) {
            if (!currentNode.getParentNode().isPresent()) {
                break;
            }
            currentNode = currentNode.getParentNode().get();
        }

        //Set the ClassOrInterface part for the strategy context
        if (currentNode instanceof CompilationUnit) {
            if (((CompilationUnit) currentNode).getStorage().isPresent()) {
                String filename = ((CompilationUnit) currentNode).getStorage().get().getFileName();
                File file = new File(filename);

                if (node.getRange().isPresent()) {
                    return new FilePart()
                            .setFile(file)
                            .setRange(node.getRange().get());
                }
            }
        }

        return null;
    }

    /**
     * This function can be used to find a AST node in a compilation unit of an other tree.
     * A use case for this is when the tree of a node doesn't have a storage component but a different
     * AST tree of the same code does
     *
     * @param compilationUnits the list of AST trees which may contain the node
     * @param node             the node for which we want a compilation unit
     * @return a compilation unit or null if non were found
     */
    public static CompilationUnit findCompilationUnitOfNode(
            final ArrayList<CompilationUnit> compilationUnits, final Node node) {

        Node currentNode = node;
        while (!(currentNode instanceof CompilationUnit)) {
            if (!currentNode.getParentNode().isPresent()) {
                break;
            }
            currentNode = currentNode.getParentNode().get();
        }

        //Set the ClassOrInterface part for the strategy context
        if (currentNode instanceof CompilationUnit) {
            for (CompilationUnit compilationUnit : compilationUnits) {
                if (compilationUnit.equals(currentNode)) {
                    return compilationUnit;
                }
            }
        }

        return null;
    }
}
