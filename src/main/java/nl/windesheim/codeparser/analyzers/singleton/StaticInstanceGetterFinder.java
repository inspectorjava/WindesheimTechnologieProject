package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.EnumSet;

/**
 * Finds static instances of itself.
 */
public class StaticInstanceGetterFinder extends VoidVisitorAdapter<Void> {
    /**
     * True if the compilation unit has a non-private static instance getter.
     */
    private boolean hasStaticInstanceGetter;

    /**
     * The name of the type the desired method should return.
     */
    private String targetType;

    /**
     * StaticInstancePropertyFinder constructor.
     *
     * @param targetType The name of the type that the desired method should return
     */
    public StaticInstanceGetterFinder(final String targetType) {
        this.hasStaticInstanceGetter = false;
        this.targetType = targetType;
    }

    /**
     * @return Whether the compilation unit has a non-private static instance method
     */
    public boolean getHasStaticInstanceGetter() {
        return hasStaticInstanceGetter;
    }

    @Override
    public void visit(final MethodDeclaration md, final Void arg) {
        super.visit(md, arg);

        // The method should be static and not private
        EnumSet<Modifier> modifiers = md.getModifiers();

        if (!modifiers.contains(Modifier.PRIVATE) && modifiers.contains(Modifier.STATIC)) {
            // The method should return the expected type
            if (md.getType().asString().equals(targetType)) {
                hasStaticInstanceGetter = true;
            }
        }
    }
}
