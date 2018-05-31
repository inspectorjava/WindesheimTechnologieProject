package nl.windesheim.codeparser.analyzers.util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Find all interfaces in files.
 */
public class FindAllInterfaces {

    /**
     * List of classOrInterfaceDeclarations.
     */
    private final List<ClassOrInterfaceDeclaration> clsOrInterfDeclrs;

    /**
     * Files to be searched.
     */
    private final List<CompilationUnit> files;

    /**
     * Default constructor.
     * @param files files to be searched.
     */
    public FindAllInterfaces(final List<CompilationUnit> files) {
        this.clsOrInterfDeclrs = new ArrayList<>();
        this.files = files;
    }

    /**
     * A easy-to-read and call static method.
     * @param files compilationunits
     * @return list of interfaces
     */
    public static List<ClassOrInterfaceDeclaration> inFiles(final List<CompilationUnit> files) {
        FindAllInterfaces finder = new FindAllInterfaces(files);
        finder.find();
        return finder.getInterfaces();
    }

    /**
     * Return the list of interfaces.
     * @return list of interfaces.
     */
    public List<ClassOrInterfaceDeclaration> getInterfaces() {
        return this.clsOrInterfDeclrs;
    }

    /**
     * Search for the interfaces.
     */
    public void find() {
        for (CompilationUnit file : this.files) {
            // Iterate over each of the class/interface declaration in the file
            for (ClassOrInterfaceDeclaration clsOrInterfDecl
                    : file.findAll(ClassOrInterfaceDeclaration.class)) {
                // Stop when the declration isnt an interface
                if (!clsOrInterfDecl.isInterface()) {
                    continue;
                }
                this.clsOrInterfDeclrs.add(clsOrInterfDecl);
            }
        }
    }
}
