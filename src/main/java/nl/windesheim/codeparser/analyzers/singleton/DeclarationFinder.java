package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public abstract class DeclarationFinder extends VoidVisitorAdapter<Void> {
    /**
     * True if the desired declaration is present in the compilation unit.
     */
    protected boolean hasDeclaration;

    /**
     * The name of the type the desired declaration should have.
     */
    protected String targetType;

    /**
     * StaticInstancePropertyFinder constructor.
     *
     * @param targetType The name of the type that the instance field should have
     */
    public DeclarationFinder (final String targetType) {
        this.hasDeclaration = false;
        this.targetType = targetType;
    }

    /**
     * @return Whether the declaration is present in the compilation unit
     */
    public boolean isHasDeclaration () {
        return this.hasDeclaration;
    }
}
