package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Abstract class DeclarationFinder.
 * Provides functionality for determining whether a JavaParser tree contains a declaration of a given type.
 */
public abstract class DeclarationFinder extends VoidVisitorAdapter<Void> {
    /**
     * True if the desired declaration is present in the compilation unit.
     */
    private boolean hasDeclaration;

    /**
     * The name of the type the desired declaration should have.
     */
    private String targetType;

    /**
     * StaticInstancePropertyFinder constructor.
     *
     * @param targetType The name of the type that the declaration should have
     */
    public DeclarationFinder(final String targetType) {
        this.hasDeclaration = false;
        this.targetType = targetType;
    }

    /**
     * @return Whether the declaration is present in the compilation unit
     */
    public boolean isHasDeclaration() {
        return this.hasDeclaration;
    }

    /**
     * Set whether a declaration has been found.
     *
     * @param hasDeclaration Whether a declaration has been found
     */
    protected void setHasDeclaration(final boolean hasDeclaration) {
        this.hasDeclaration = hasDeclaration;
    }

    /**
     * @return The desired type of the declaration
     */
    protected String getTargetType() {
        return this.targetType;
    }
}
