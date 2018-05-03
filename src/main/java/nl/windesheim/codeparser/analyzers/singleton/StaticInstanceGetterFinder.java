package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.EnumSet;

/**
 * Finds static instances of itself.
 */
public class StaticInstanceGetterFinder extends DeclarationFinder {
    /**
     * @inheritDoc
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
            if (md.getType().asString().equals(this.targetType)) {
                this.hasDeclaration = true;
            }
        }
    }
}
