package nl.windesheim.codeparser.analyzers.util.visitor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Searches for classes which implement the given interface.
 */
public class ImplementationOrSuperclassFinder extends VoidVisitorAdapter<ClassOrInterfaceDeclaration> {

    /**
     * The classes which implement the given interface.
     */
    private final List<ClassOrInterfaceDeclaration> classes = new ArrayList<>();

    /**
     * A list of errors which were encountered.
     */
    private final List<Exception> errors = new ArrayList<>();

    /**
     * @return A list of classes which extend the given class.
     */
    public List<ClassOrInterfaceDeclaration> getClasses() {
        return classes;
    }

    /**
     * @return a list of errors which were encountered
     */
    public List<Exception> getErrors() {
        return errors;
    }

    /**
     * Resets the list of classes.
     */
    public void reset() {
        errors.clear();
        classes.clear();
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration classToCheck,
                      final ClassOrInterfaceDeclaration classDeclaration) {
        super.visit(classToCheck, classDeclaration);

        if (classDeclaration.isInterface()) {
            interfaceCheck(classToCheck, classDeclaration);
        } else {
            abstractClassCheck(classToCheck, classDeclaration);
        }
    }

    /**
     * @param classToCheck     class to check
     * @param classDeclaration class declaration
     */
    private void interfaceCheck(
            final ClassOrInterfaceDeclaration classToCheck,
            final ClassOrInterfaceDeclaration classDeclaration
    ) {
        for (ClassOrInterfaceType type : classToCheck.getImplementedTypes()) {

            ResolvedReferenceTypeDeclaration solved;
            try {
                solved = type.resolve().getTypeDeclaration();
            } catch (UnsolvedSymbolException e) {
                errors.add(e);
                continue;
            }

            if (!(solved instanceof JavaParserInterfaceDeclaration)) {
                continue;
            }

            if (((JavaParserInterfaceDeclaration) solved).getWrappedNode().equals(classDeclaration)) {
                classes.add(classToCheck);
            }
        }
    }

    /**
     * @param classToCheck     class to check
     * @param classDeclaration class declaration
     */
    private void abstractClassCheck(
            final ClassOrInterfaceDeclaration classToCheck,
            final ClassOrInterfaceDeclaration classDeclaration
    ) {
        for (ClassOrInterfaceType type : classToCheck.getExtendedTypes()) {

            ResolvedReferenceTypeDeclaration solved;
            try {
                solved = type.resolve().getTypeDeclaration();
            } catch (UnsolvedSymbolException e) {
                errors.add(e);
                continue;
            }

            if (!(solved instanceof JavaParserClassDeclaration)) {
                continue;
            }

            if (((JavaParserClassDeclaration) solved).getWrappedNode().equals(classDeclaration)) {
                classes.add(classToCheck);
            }
        }
    }
}
