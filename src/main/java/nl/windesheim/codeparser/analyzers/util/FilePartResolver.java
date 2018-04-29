package nl.windesheim.codeparser.analyzers.util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import nl.windesheim.codeparser.FilePart;

import java.io.File;

public class FilePartResolver {

    /**
     * @param node The node you want the FilePart of
     * @return the FilePart of the node
     */
    public static FilePart getFilePartOfNode(final Node node){

        Node currentNode = node;
        while (!(currentNode instanceof CompilationUnit)){
            if (!currentNode.getParentNode().isPresent()){
                break;
            }
            currentNode = currentNode.getParentNode().get();
        }

        //Set the Class part for the strategy context
        if (currentNode instanceof CompilationUnit){
            if (((CompilationUnit) currentNode).getStorage().isPresent()){
                String filename = ((CompilationUnit) currentNode).getStorage().get().getFileName();
                File file = new File(filename);

                if (node.getRange().isPresent()){
                    return new FilePart()
                                .setFile(file)
                                .setRange(node.getRange().get());
                }
            }
        }

        return null;
    }
}
