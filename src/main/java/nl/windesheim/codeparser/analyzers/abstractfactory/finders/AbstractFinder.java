package nl.windesheim.codeparser.analyzers.abstractfactory.finders;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Helper method to find different sorts of Abstract Factories.
 */
public abstract class AbstractFinder {

    /**
     * The implementation finder.
     */
    private final ImplementationOrSuperclassFinder implFinder;

    /**
     * Constructor to create a new instance of the implementation finder.
     */
    public AbstractFinder() {
        this.implFinder = new ImplementationOrSuperclassFinder();
    }

    /**
     * Find all the (abstract) factory implementations.
     * @param declarations List of all the declarations.
     * @return List of all the found patterns.
     */
    public abstract List<ClassOrInterfaceDeclaration> find(List<ClassOrInterfaceDeclaration> declarations);

    /**
     * Find implementations of a ClassOrInterfaceDeclaration file.
     *
     * @param factory      The Class or interface we want to check
     * @param declarations List of all the declarations
     * @return the implementations of the given declaration.
     */
    public Collection<? extends ClassOrInterfaceDeclaration>
    findImplementations(final ClassOrInterfaceDeclaration factory,
                        final List<ClassOrInterfaceDeclaration> declarations) {
        this.implFinder.reset();
        for (ClassOrInterfaceDeclaration allClass : declarations) {
            this.implFinder.visit(allClass, factory);
        }
        return this.implFinder.getClasses();
    }

    /**
     * Recursively find all the method declarations inside a class.
     *
     * @param declaration The declaration to search in.
     * @param methods     A list of all the found declarations so far.
     * @return A list of all the found declarations.
     */
    private List<MethodDeclaration> findMethodDeclarations(final ClassOrInterfaceDeclaration declaration,
                                                           final List<MethodDeclaration> methods) {
        for (Node node : declaration.getChildNodes()) {
            if (node instanceof MethodDeclaration) {
                MethodDeclaration method = (MethodDeclaration) node;
                methods.add(method);
            } else if (node instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration classOrInterface = (ClassOrInterfaceDeclaration) node;
                this.findMethodDeclarations(classOrInterface, methods);
            }
        }
        return methods;
    }

    /**
     * Find all the factory classes. These are all the classes that implement or extend the factory classes.
     *
     * @param declarations           List of all the file declarations.
     * @param implementations List of all the abstract factory pattern implementations
     * @return A list with all the factory classes.
     */
    protected List<ClassOrInterfaceDeclaration>
    findFactoryClasses(final List<ClassOrInterfaceDeclaration> declarations,
                       final List<ClassOrInterfaceDeclaration> implementations) {
        ArrayList<ClassOrInterfaceDeclaration> factoryClasses = new ArrayList<>();
        for (ClassOrInterfaceDeclaration declaration : declarations) {
            List<MethodDeclaration> methods = new ArrayList<>();
            methods = this.findMethodDeclarations(declaration, methods);

            for (MethodDeclaration method : methods) {
                this.findAndFillFactoryClasses(implementations, factoryClasses, method);
            }
        }
        return factoryClasses;
    }

    /**
     * Find all the factory implementations and fill the given list.
     * @param implementations List of factory implementations.
     * @param factoryClasses List of the factory classes.
     * @param method The method we have to search.
     */
    private void findAndFillFactoryClasses(
            final List<ClassOrInterfaceDeclaration> implementations,
            final List<ClassOrInterfaceDeclaration> factoryClasses,
            final MethodDeclaration method) {
        for (ClassOrInterfaceDeclaration implementation : implementations) {
            if (!(method.getType() instanceof ClassOrInterfaceType)) {
                continue;
            }
            ClassOrInterfaceType type = (ClassOrInterfaceType) method.getType();
            ResolvedReferenceTypeDeclaration typeDeclaration =
                    (ResolvedReferenceTypeDeclaration) type.resolve().getTypeDeclaration();

            //If the type is a interface
            if (typeDeclaration instanceof JavaParserInterfaceDeclaration) {
                ClassOrInterfaceDeclaration resolvedInterface =
                        ((JavaParserInterfaceDeclaration) typeDeclaration).getWrappedNode();
                String declarationName = resolvedInterface.asClassOrInterfaceDeclaration().getNameAsString();
                String interfaceName = implementation.getImplementedTypes().get(0).getNameAsString();
                if (declarationName.equals(interfaceName)
                        && !factoryClasses.contains(resolvedInterface.asClassOrInterfaceDeclaration())) {
                    factoryClasses.add(resolvedInterface.asClassOrInterfaceDeclaration());
                }
            } else if (typeDeclaration instanceof JavaParserClassDeclaration) {
                ClassOrInterfaceDeclaration resolvedInterface =
                        ((JavaParserClassDeclaration) typeDeclaration).getWrappedNode();
                String declarationName = resolvedInterface.asClassOrInterfaceDeclaration().getNameAsString();
                String interfaceName = implementation.getExtendedTypes().get(0).getNameAsString();
                if (declarationName.equals(interfaceName)
                        && !factoryClasses.contains(resolvedInterface.asClassOrInterfaceDeclaration())) {
                    factoryClasses.add(resolvedInterface.asClassOrInterfaceDeclaration());
                }
            }
        }
    }


    /**
     * Simple log helper to output some text.
     *
     * @param message the message to print.
     */
    protected void log(final String message) {
        System.out.println(message);
    }
}
