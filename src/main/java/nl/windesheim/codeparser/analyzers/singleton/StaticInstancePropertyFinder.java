package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.EnumSet;

/**
 * Finds static instances of itself.
 */
public class StaticInstancePropertyFinder extends DeclarationFinder {

    /**
     * StaticInstancePropertyFinder constructor.
     *
     * @param targetType The name of the type that the declaration should have
     */
    public StaticInstancePropertyFinder(final String targetType) {
        super(targetType);
    }

    @Override
    public void visit(final FieldDeclaration fieldDeclaration, final Void argument) {
        super.visit(fieldDeclaration, argument);

        // The field should be private and static
        EnumSet<Modifier> modifiers = fieldDeclaration.getModifiers();

        if (modifiers.contains(Modifier.PRIVATE) && modifiers.contains(Modifier.STATIC)) {
            // The field should be of the expected type
            VariableDeclarator variable = fieldDeclaration.getVariable(0);

            if (variable != null && variable.getType().asString().equals(this.getTargetType())) {
                this.setHasDeclaration(true);
            }
        }
    }
}
