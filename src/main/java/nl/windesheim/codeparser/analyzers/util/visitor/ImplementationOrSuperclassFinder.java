package nl.windesheim.codeparser.analyzers.util.visitor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Searches for classes which implement the given interface.
 */
public class ImplementationOrSuperclassFinder extends VoidVisitorAdapter<ClassOrInterfaceDeclaration> {

    /**
     * The classes which implement the given interface.
     */
    private List<ClassOrInterfaceDeclaration> classes;

    /**
     * The type solver is used find referenced classes.
     */
    private final TypeSolver typeSolver;

    /**
     * Make a new ImplementationOrSuperclassFinder.
     * @param typeSolver a type solver
     */
    public ImplementationOrSuperclassFinder(final TypeSolver typeSolver) {
        super();

        classes = new ArrayList<>();
        this.typeSolver = typeSolver;
    }

    /**
     * @return A list of classes which extend the given class.
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
                      final ClassOrInterfaceDeclaration classDeclaration) {
        super.visit(classToCheck, classDeclaration);

        if (classDeclaration.isInterface()) {
            for (ClassOrInterfaceType type : classToCheck.getImplementedTypes()) {

                ResolvedReferenceTypeDeclaration solved = typeSolver.solveType(type.getNameAsString());
                if (!(solved instanceof JavaParserInterfaceDeclaration)) {
                    continue;
                }

                if (((JavaParserInterfaceDeclaration) solved).getWrappedNode().equals(classDeclaration)) {
                    classes.add(classToCheck);
                }
            }
        } else {
            for (ClassOrInterfaceType type : classToCheck.getExtendedTypes()) {

                ResolvedReferenceTypeDeclaration solved = typeSolver.solveType(type.getNameAsString());
                if (!(solved instanceof JavaParserClassDeclaration)) {
                    continue;
                }

                if (((JavaParserClassDeclaration) solved).getWrappedNode().equals(classDeclaration)) {
                    classes.add(classToCheck);
                }
            }
        }
    }
}
