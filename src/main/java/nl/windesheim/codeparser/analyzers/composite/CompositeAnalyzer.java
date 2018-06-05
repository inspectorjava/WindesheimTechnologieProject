package nl.windesheim.codeparser.analyzers.composite;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
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

    /**
     * A visitor which finds references of lists with generic type of itself.
     */
    private final FindSelfReferringListDeclaration selfRefVisitor;

    /**
     * A visitor which finds implementations or subclasses of a interface or class.
     */
    private final ImplementationOrSuperclassFinder implFinder;

    /**
     * Default constructor.
     */
    public CompositeAnalyzer() {
        super();

        selfRefVisitor = new FindSelfReferringListDeclaration();
        implFinder = new ImplementationOrSuperclassFinder();
    }

    @Override
    public List<IDesignPattern> analyze(final List<CompilationUnit> files) {
        getErrorLog().clearErrors();

        List<IDesignPattern> designPatterns = new ArrayList<>();

        // Get all interfaces
        List<ClassOrInterfaceDeclaration> allInterfaceDeclr = FindAllInterfaces.inFiles(files);


        for (ClassOrInterfaceDeclaration interfaceDeclr : allInterfaceDeclr) {

            implFinder.reset();

            for (CompilationUnit file : files) {
                implFinder.visit(file, interfaceDeclr);
            }

            List<ClassOrInterfaceDeclaration> interfaceImpl = implFinder.getClasses();

            // Declare lists of potential leafs and composites
            List<ClassOrInterfaceDeclaration> potLeafs = new ArrayList<>();
            List<ClassOrInterfaceDeclaration> potComposites = new ArrayList<>();

            // Find list<> in each of the classes that implement it
            for (ClassOrInterfaceDeclaration interfaceIntr : interfaceImpl) {
                selfRefVisitor.reset();
                selfRefVisitor.visit(interfaceIntr, interfaceDeclr);
                if (selfRefVisitor.getFieldDeclerations().size() > 0) {
                    potComposites.add(interfaceIntr);
                } else {
                    potLeafs.add(interfaceIntr);
                }

                for (Exception exception : selfRefVisitor.getErrors()) {
                    getErrorLog().addError(exception);
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
     *
     * @param component  found component
     * @param composites found composites
     * @param leafs      found leafs
     * @return CompositePattern
     */
    private CompositePattern createComposite(
            final ClassOrInterfaceDeclaration component,
            final List<ClassOrInterfaceDeclaration> composites,
            final List<ClassOrInterfaceDeclaration> leafs
    ) {
        ClassOrInterface componentClass = new ClassOrInterface()
                .setDeclaration(component)
                .setFilePart(FilePartResolver.getFilePartOfNode(component))
                .setName(component.getNameAsString());

        List<ClassOrInterface> compositeClasses = new ArrayList<>();
        for (ClassOrInterfaceDeclaration declaration : composites) {
            compositeClasses.add(
                    new ClassOrInterface()
                            .setDeclaration(declaration)
                            .setFilePart(FilePartResolver.getFilePartOfNode(declaration))
                            .setName(declaration.getNameAsString())
            );
        }

        List<ClassOrInterface> leafClasses = new ArrayList<>();
        for (ClassOrInterfaceDeclaration declaration : leafs) {
            leafClasses.add(
                    new ClassOrInterface()
                            .setDeclaration(declaration)
                            .setFilePart(FilePartResolver.getFilePartOfNode(declaration))
                            .setName(declaration.getNameAsString())
            );
        }

        return new CompositePattern(componentClass, compositeClasses, leafClasses);
    }
}
