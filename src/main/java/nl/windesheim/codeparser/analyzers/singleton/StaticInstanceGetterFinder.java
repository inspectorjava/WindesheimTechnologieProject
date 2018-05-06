package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.EnumSet;

/**
 * Finds getters of static instances of itself.
 */
public class StaticInstanceGetterFinder extends DeclarationFinder {
    /**
     * StaticInstanceGetterFinder constructor.
     *
     * @param targetType The name of the type that the declaration should have
     */
    public StaticInstanceGetterFinder(final String targetType) {
        super(targetType);
    }

    @Override
    public void visit(final MethodDeclaration md, final Void arg) {
        super.visit(md, arg);

        // The method should be static and not private
        EnumSet<Modifier> modifiers = md.getModifiers();

        if (!modifiers.contains(Modifier.PRIVATE) && modifiers.contains(Modifier.STATIC)) {
            // The method should return the expected type
            if (md.getType().asString().equals(this.getTargetType())) {
                this.setHasDeclaration(true);
            }
        }
    }
}
