package nl.windesheim.codeparser.analyzers.abstractfactory;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.abstractfactory.finders.InterfaceFactoryFinder;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
import nl.windesheim.codeparser.patterns.AbstractFactory;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * The full class to analyze compilation units to check if an (Abstract) Factory pattern is used.
 */
public class AbstractFactoryAnalyzer extends PatternAnalyzer {

    @Override
    public List<IDesignPattern> analyze(final List<CompilationUnit> files) {
        ArrayList<IDesignPattern> patterns = new ArrayList<>();

        if (files.isEmpty()) {
            return patterns;
        }

        InterfaceFactoryFinder interfaceFinder = new InterfaceFactoryFinder();

        List<ClassOrInterfaceDeclaration> declarations = this.findDeclarations(files);

        List<ClassOrInterfaceDeclaration> factoryClasses = new ArrayList<>();

        factoryClasses.addAll(interfaceFinder.find(declarations));

        // Removing the abstract factory finder since it was deemed not necessary by our product owner.
//        AbstractFactoryFinder abstractFinder   = new AbstractFactoryFinder();
//        factoryClasses.addAll(abstractFinder.find(declarations));

        for (ClassOrInterfaceDeclaration factory : factoryClasses) {
            List<ClassOrInterfaceDeclaration> implementations =
                    interfaceFinder.findImplementations(factory, declarations);
            patterns.add(makeAbstractFactory(factory, implementations));
        }

        return patterns;
    }

    /**
     * Find all the ClassOrInterface declarations based on all the compilation units.
     *
     * @param files List of all the compilation units.
     * @return List of all the ClassOrInterface declarations
     */
    private List<ClassOrInterfaceDeclaration> findDeclarations(final List<CompilationUnit> files) {
        List<ClassOrInterfaceDeclaration> declarations = new ArrayList<>();
        for (CompilationUnit compilationUnit : files) {
            for (Node node : compilationUnit.getChildNodes()) {
                if (node instanceof ClassOrInterfaceDeclaration) {
                    declarations.add((ClassOrInterfaceDeclaration) node);
                }
            }
        }
        return declarations;
    }

    /**
     * Builds a AbstractFactory object.
     *
     * @param factory         the factory class
     * @param implementations it's implementations
     * @return AbstractFactory
     */
    private AbstractFactory makeAbstractFactory(
            final ClassOrInterfaceDeclaration factory,
            final List<ClassOrInterfaceDeclaration> implementations
    ) {
        AbstractFactory abstractFactory = new AbstractFactory();
        abstractFactory.setFactoryInterface(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(factory))
                        .setName(factory.getNameAsString())
                        .setDeclaration(factory)
        );

        List<ClassOrInterface> implClasses = new ArrayList<>();
        for (ClassOrInterfaceDeclaration declaration : implementations) {
            implClasses.add(
                    new ClassOrInterface()
                            .setFilePart(FilePartResolver.getFilePartOfNode(declaration))
                            .setName(declaration.getNameAsString())
                            .setDeclaration(declaration)
            );
        }

        abstractFactory.setImplementations(implClasses);
        return abstractFactory;
    }
}
