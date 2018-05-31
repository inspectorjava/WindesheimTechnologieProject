package nl.windesheim.codeparser.analyzers.composite;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.util.FindAllInterfaces;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;
import nl.windesheim.codeparser.patterns.CompositePattern;
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

        List<IDesignPattern> designPatterns = new ArrayList<>();

        // Get all interfaces
        List<ClassOrInterfaceDeclaration> allInterfaceDeclarations = FindAllInterfaces.inFiles(files);


        for (ClassOrInterfaceDeclaration interfaceDeclaration : allInterfaceDeclarations) {
            ImplementationOrSuperclassFinder implFinder = new ImplementationOrSuperclassFinder();
            for (CompilationUnit file : files) {
                implFinder.visit(file, interfaceDeclaration);
            }

            List<ClassOrInterfaceDeclaration> interfaceImplementations = implFinder.getClasses();

            // Declare lists of possible leafs and composites
            List<ClassOrInterfaceDeclaration> potentialFoundLeafs = new ArrayList<>();
            List<ClassOrInterfaceDeclaration> potentialFoundComposites = new ArrayList<>();

            // Find list<> in each of the classes that implement it
            for (ClassOrInterfaceDeclaration interfaceImplementation : interfaceImplementations) {
                FindSelfReferringListDeclaration selfReferringVisitor = new FindSelfReferringListDeclaration();
                selfReferringVisitor.visit(interfaceImplementation, interfaceDeclaration);
                if (selfReferringVisitor.getFieldDeclerations().size() > 0) {
                    potentialFoundComposites.add(interfaceImplementation);
                } else {
                    potentialFoundLeafs.add(interfaceImplementation);
                }
            }

            if (potentialFoundComposites.size() > 0) {
                CompositePattern compositePattern
                        = createComposite(interfaceDeclaration, potentialFoundComposites, potentialFoundLeafs);
                designPatterns.add(compositePattern);
            }
        }

        return designPatterns;
    }


    /**
     * Create a composite pattern.
     * @param component found component
     * @param composites found composites
     * @param leafs found leafs
     * @return CompositePattern
     */
    private CompositePattern createComposite(
            final ClassOrInterfaceDeclaration component,
            final List<ClassOrInterfaceDeclaration> composites,
            final List<ClassOrInterfaceDeclaration> leafs
    ) {
        return new CompositePattern(component, composites, leafs);
    }
}
