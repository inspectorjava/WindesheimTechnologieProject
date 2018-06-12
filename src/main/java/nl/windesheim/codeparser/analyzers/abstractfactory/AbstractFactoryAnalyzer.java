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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        for (ClassOrInterfaceDeclaration factory : factoryClasses) {
            List<ClassOrInterfaceDeclaration> implementations =
                    interfaceFinder.findImplementations(factory, declarations);
            Map<ClassOrInterfaceDeclaration, List<ClassOrInterfaceDeclaration>> factoryInterfaces =
                    interfaceFinder.findInterfacesFromFactory(implementations, declarations);
            patterns.add(makeAbstractFactory(factory, implementations, factoryInterfaces));
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
     * @param factoryInterfaces the factory interfaces
     * @return AbstractFactory
     */
    private AbstractFactory makeAbstractFactory(
            final ClassOrInterfaceDeclaration factory,
            final List<ClassOrInterfaceDeclaration> implementations,
            final Map<ClassOrInterfaceDeclaration, List<ClassOrInterfaceDeclaration>> factoryInterfaces) {
        // The factory interface. (KingdomFactory)
        AbstractFactory abstractFactory = new AbstractFactory();
        abstractFactory.setFactoryInterface(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(factory))
                        .setName(factory.getNameAsString())
                        .setDeclaration(factory)
        );

        // The factory implementations (OrcKingdomFactory, ElfKingdomFactory)
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

        HashMap<ClassOrInterface, List<ClassOrInterface>>
                implInterfaces = new HashMap<>();
        for (
                Map.Entry<ClassOrInterfaceDeclaration, List<ClassOrInterfaceDeclaration>> entry
                : factoryInterfaces.entrySet()) {

            List<ClassOrInterface> products = new ArrayList<>();

            for (ClassOrInterfaceDeclaration declaration : entry.getValue()) {
                products.add(new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(declaration))
                        .setName(declaration.getNameAsString())
                        .setDeclaration(declaration));
            }

            implInterfaces.put(
                    new ClassOrInterface()
                            .setFilePart(FilePartResolver.getFilePartOfNode(entry.getKey()))
                            .setName(entry.getKey().getNameAsString())
                            .setDeclaration(entry.getKey()),
                    products
            );
        }
        abstractFactory.setConcreteInterfaces(implInterfaces);

        return abstractFactory;
    }
}
