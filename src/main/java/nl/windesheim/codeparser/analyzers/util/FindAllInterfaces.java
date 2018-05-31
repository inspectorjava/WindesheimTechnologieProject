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
     * List of classOrInterfaceDeclarations
     */
    private List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations;

    /**
     * Files to be searched.
     */
    private List<CompilationUnit> files;

    /**
     * Default constructor.
     * @param files files to be searched.
     */
    public FindAllInterfaces(final List<CompilationUnit> files) {
        this.classOrInterfaceDeclarations = new ArrayList<>();
        this.files = files;
    }

    /**
     * A easy-to-read and call static method.
     * @param files
     * @return
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
        return this.classOrInterfaceDeclarations;
    }

    /**
     * Search for the interfaces.
     */
    public void find() {
        for (CompilationUnit file : this.files) {
            // Iterate over each of the class/interface declaration in the file
            for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : file.findAll(ClassOrInterfaceDeclaration.class)) {
                // Stop when the declration isnt an interface
                if (!classOrInterfaceDeclaration.isInterface()) {
                    continue;
                }
                this.classOrInterfaceDeclarations.add(classOrInterfaceDeclaration);
            }
        }
    }
}
