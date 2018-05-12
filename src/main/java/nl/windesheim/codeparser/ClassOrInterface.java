package nl.windesheim.codeparser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

/**
 * A object which describes a class.
 */
public class Class {
    /**
     * The name of the class.
     */
    private String className;

    /**
     * The filePart in which this class is found
     */
    private FilePart filePart;

    /**
     * The AST node which was found
     */
    private ClassOrInterfaceDeclaration classDeceleration;

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
    public Class setFilePart(final FilePart filePart) {
        this.filePart = filePart;
        return this;
    }

    /**
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the class name to be set
     * @return this
     */
    public Class setClassName(final String className) {
        this.className = className;
        return this;
    }

    /**
     * @return the class deceleration
     */
    public ClassOrInterfaceDeclaration getClassDeceleration() {
        return classDeceleration;
    }

    /**
     * @param classDeceleration the class deceleration to be set
     * @return this
     */
    public Class setClassDeceleration(final ClassOrInterfaceDeclaration classDeceleration) {
        this.classDeceleration = classDeceleration;
        return this;
    }
}
