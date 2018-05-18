package nl.windesheim.codeparser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

/**
 * A object which describes a class.
 */
public class ClassOrInterface {
    /**
     * The name of the class.
     */
    private String name;

    /**
     * The filePart in which this class is found.
     */
    private FilePart filePart;

    /**
     * The AST node which was found.
     */
    private ClassOrInterfaceDeclaration declaration;

    /**
     * @return the filepart
     */
    public FilePart getFilePart() {
        return filePart;
    }

    /**
     * @param filePart the file part to be set
     * @return this
     */
    public ClassOrInterface setFilePart(final FilePart filePart) {
        this.filePart = filePart;
        return this;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to be set
     * @return this
     */
    public ClassOrInterface setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * @return the declaration
     */
    public ClassOrInterfaceDeclaration getDeclaration() {
        return declaration;
    }

    /**
     * @param declaration the declaration to be set
     * @return this
     */
    public ClassOrInterface setDeclaration(final ClassOrInterfaceDeclaration declaration) {
        this.declaration = declaration;
        return this;
    }

    @Override
    public String toString() {
        return "ClassOrInterface{"
                + "name='" + name + '\''
                + ", filePart=" + filePart
                + '}';
    }
}
