package nl.windesheim.codeparser.analyzers.singleton;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.EnumSet;

public class StaticInstancePropertyFinder extends VoidVisitorAdapter<Void> {
    /**
     * True if the compilation unit has a private static instance property
     */
    private boolean hasStaticInstanceProperty;

    /**
     * The name of the type the instance property should have
     */
    private String targetType;

    /**
     * StaticInstancePropertyFinder constructor.
     * @param targetType The name of the type that the instance field should have
     */
    public StaticInstancePropertyFinder (String targetType){
        this.hasStaticInstanceProperty = false;
        this.targetType = targetType;
    }

    /**
     * @return Whether the compilation unit has a private static instance property
     */
    public boolean getHasStaticInstanceProperty (){
        return hasStaticInstanceProperty;
    }

    @Override
    public void visit (FieldDeclaration fd, Void arg){
        // FIXME Only check fields that belong directly to the investigated class
        super.visit(fd, arg);

        // The field should be private and static
        EnumSet<Modifier> modifiers = fd.getModifiers();

        if (modifiers.contains(Modifier.PRIVATE) && modifiers.contains(Modifier.STATIC)) {
            // The field should be of the expected type
            VariableDeclarator variable = fd.getVariable(0);

            if (variable != null && variable.getType().asString().equals(targetType)) {
                hasStaticInstanceProperty = true;
            }
        }
    }
}
