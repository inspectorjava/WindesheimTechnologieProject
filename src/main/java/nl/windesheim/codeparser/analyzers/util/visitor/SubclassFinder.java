package nl.windesheim.codeparser.analyzers.util.visitor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Searches for classes which extend the given class.
 */
public class SubclassFinder extends VoidVisitorAdapter<ClassOrInterfaceDeclaration> {

    /**
     * The classes which extend the given class.
     */
    private List<ClassOrInterfaceDeclaration> classes;

    /**
     * The type solver is used find referenced classes.
     */
    private final TypeSolver typeSolver;

    /**
     * Make a new SubclassFinder.
     * @param typeSolver a type solver
     */
    public SubclassFinder(final TypeSolver typeSolver) {
        super();

        classes = new ArrayList<>();
        this.typeSolver = typeSolver;
    }

    /**
     * @return A list of classes which implement the given interface.
     */
    public List<ClassOrInterfaceDeclaration> getClasses() {
        return classes;
    }

    /**
     * Resets the list of classes.
     */
    public void reset() {
        classes = new ArrayList<>();
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration classToCheck,
                      final ClassOrInterfaceDeclaration iDeclaration) {
        super.visit(classToCheck, iDeclaration);

        for (ClassOrInterfaceType type : classToCheck.getExtendedTypes()) {

            ResolvedReferenceTypeDeclaration solved = typeSolver.solveType(type.getNameAsString());
            if (!(solved instanceof JavaParserClassDeclaration)) {
                continue;
            }

            if (((JavaParserClassDeclaration) solved).getWrappedNode().equals(iDeclaration)) {
                classes.add(classToCheck);
            }
        }
    }
}
