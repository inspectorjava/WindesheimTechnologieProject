package nl.windesheim.codeparser.analyzers.composite;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Find a objects with lists of their own objects.
 */
public class CompositeAnalyzer extends PatternAnalyzer {

    @Override

    /* A composite class is made of the following components

    - A composite class
        declares interface for objects in composition.
        implements default behaviour for the interface common to all classes as appropriate.
        declares an interface for accessing and managing its child components.
    - A base class or interface
        defines behaviour for components having children.
        stores child components.
        implements child related operations in the component interface.
    - Leaf classes
        represents leaf objects in the composition.
        A leaf has no children.
        defines behaviour for primitive objects in the composition
     */

    public List<IDesignPattern> analyze(final List<CompilationUnit> files) {

        FindSelfReferringListDeclaration selfReferringVisitor = new FindSelfReferringListDeclaration();



        // Find component class

        // 1. Find all interfaces.
        // 2. Find all implementations of interface.
        // 3. Find list implementations in implementations of interface.
        // 4. Check if list type is of interface type of 1.
        // 5. All implementations (3.) that aren't using a list of interface (1.) are leafs

        for (CompilationUnit file : files) {
            for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : file.findAll(ClassOrInterfaceDeclaration.class)) {
                if (!classOrInterfaceDeclaration.isInterface()) {
                    continue;
                }

                ImplementationOrSuperclassFinder implFinder = new ImplementationOrSuperclassFinder();
                //selfReferringVisitor.visit(classOrInterfaceDeclaration, classOrInterfaceDeclaration);
            }
        }

        List<FieldDeclaration> fieldDeclarations = selfReferringVisitor.getFieldDeclerations();

        //System.out.println(fieldDeclarations);

        return new ArrayList<>();
    }
}
