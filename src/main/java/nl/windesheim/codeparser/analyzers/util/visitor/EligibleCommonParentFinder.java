package nl.windesheim.codeparser.analyzers.chainofresponsibility;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds eligible 'common parent' classes.
 */
public class EligibleCommonParentFinder extends VoidVisitorAdapter<CombinedTypeSolver> {

    /**
     * The list of classes which were found.
     */
    private List<ClassOrInterfaceDeclaration> commonParents;

    /**
     * Creates a new EligibleCommonParentFinder.
     */
    public EligibleCommonParentFinder() {
        super();

        reset();
    }

    /**
     * Resets the result.
     */
    public void reset() {
        commonParents = new ArrayList<>();
    }

    /**
     * @return the found common parents
     */
    public List<ClassOrInterfaceDeclaration> getClasses() {
        return commonParents;
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration declaration, final CombinedTypeSolver typeSolver) {
        super.visit(declaration, typeSolver);

        boolean isInterface = declaration.isInterface();

        //The common parent should be a interface or abstract class
        // if not one of the above stop processing
        if (!isInterface && !declaration.getModifiers().contains(Modifier.ABSTRACT)) {
            return;
        }

        //The common parent should have at least on method
        if (declaration.getMethods().isEmpty()) {
            return;
        }

        //if the common parent is a abstract class it should at least have one abstract method
        if (!isInterface) {
            boolean hasAbstractMethod = false;

            for (MethodDeclaration methodDeclaration : declaration.getMethods()) {
                if (methodDeclaration.getModifiers().contains(Modifier.ABSTRACT)) {
                    hasAbstractMethod = true;
                }
            }

            if (!hasAbstractMethod) {
                return;
            }
        }

        commonParents.add(declaration);
    }
}

