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
    public List<IDesignPattern> analyze(final List<CompilationUnit> files) {

        List<IDesignPattern> designPatterns = new ArrayList<>();

        // Get all interfaces
        List<ClassOrInterfaceDeclaration> allInterfaceDeclr = FindAllInterfaces.inFiles(files);


        for (ClassOrInterfaceDeclaration interfaceDeclr : allInterfaceDeclr) {
            ImplementationOrSuperclassFinder implFinder = new ImplementationOrSuperclassFinder();
            for (CompilationUnit file : files) {
                implFinder.visit(file, interfaceDeclr);
            }

            List<ClassOrInterfaceDeclaration> interfaceImpl = implFinder.getClasses();

            // Declare lists of potential leafs and composites
            List<ClassOrInterfaceDeclaration> potLeafs = new ArrayList<>();
            List<ClassOrInterfaceDeclaration> potComposites = new ArrayList<>();

            // Find list<> in each of the classes that implement it
            for (ClassOrInterfaceDeclaration interfaceIntr : interfaceImpl) {
                FindSelfReferringListDeclaration selfRefVisitor = new FindSelfReferringListDeclaration();
                selfRefVisitor.visit(interfaceIntr, interfaceDeclr);
                if (selfRefVisitor.getFieldDeclerations().size() > 0) {
                    potComposites.add(interfaceIntr);
                } else {
                    potLeafs.add(interfaceIntr);
                }
            }

            if (!potComposites.isEmpty()) {
                CompositePattern compositePattern
                        = createComposite(interfaceDeclr, potComposites, potLeafs);
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
