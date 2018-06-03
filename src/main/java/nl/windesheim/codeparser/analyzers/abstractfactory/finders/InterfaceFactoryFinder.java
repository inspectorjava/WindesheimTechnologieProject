package nl.windesheim.codeparser.analyzers.abstractfactory.finders;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Find all the interface factories.
 */
public class InterfaceFactoryFinder extends AbstractFinder {

    /**
     * The implementation finder.
     */
    private final ImplementationOrSuperclassFinder implFinder;

    /**
     * Get the implementation finder.
     */
    public InterfaceFactoryFinder() {
        super();
        this.implFinder = new ImplementationOrSuperclassFinder();
    }

    @Override
    public List<ClassOrInterfaceDeclaration> find(final List<ClassOrInterfaceDeclaration> declarations) {
        ArrayList<ClassOrInterfaceDeclaration> factoryClasses = new ArrayList<>();
        // If there are no interfaces, it is never possible to have an abstract factory pattern.
        // If there are interfaces, but they are not implemented. Ignore this pattern too.
        if (this.hasInterfaces(declarations) && this.interfacesAreImplemented(declarations)) {
            // These are all the factory interfaces
            List<ClassOrInterfaceDeclaration> factoryInterfaces = this.findFactoryInterfaces(declarations);
            this.log("I was able to find " + factoryInterfaces.size() + " of factory interfaces.");

            List<ClassOrInterfaceDeclaration> implementations =
                    this.findFactoryImplementations(factoryInterfaces, declarations);
            this.log("I was able to find " + implementations.size() + " of factory implementations.");

            factoryClasses.addAll(this.findFactoryClasses(declarations, implementations));
            this.log("I was able to find " + factoryClasses.size() + " Factory classes.");
        }

        return factoryClasses;
    }

    /**
     * Find the implementations of all the given factory interfaces.
     *
     * @param factoryInterfaces A list of the found factory interfaces.
     * @param declarations      A list of all the found declarations.
     * @return A list of all the found factory implementations.
     */
    private List<ClassOrInterfaceDeclaration>
    findFactoryImplementations(final List<ClassOrInterfaceDeclaration> factoryInterfaces,
                               final List<ClassOrInterfaceDeclaration> declarations) {
        ArrayList<ClassOrInterfaceDeclaration> implementations = new ArrayList<>();

        for (ClassOrInterfaceDeclaration factory : factoryInterfaces) {
            implementations.addAll(this.findImplementations(factory, declarations));
        }

        return implementations;
    }


    /**
     * Check if at least 1 of the declarations is an interface.
     *
     * @param declarations List of the declarations
     * @return boolean if at least 1 declaration is an interface.
     */
    private boolean hasInterfaces(final List<ClassOrInterfaceDeclaration> declarations) {
        for (ClassOrInterfaceDeclaration declaration : declarations) {
            if (declaration.isInterface()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the interfaces are at least implemented in 1 or more files.
     *
     * @param declarations List of the declarations
     * @return A boolean if the interface(s) is/are implemented.
     */
    private boolean interfacesAreImplemented(final List<ClassOrInterfaceDeclaration> declarations) {
        for (ClassOrInterfaceDeclaration declaration : declarations) {
            if (!declaration.isInterface()) {
                continue;
            }

            this.implFinder.reset();
            for (ClassOrInterfaceDeclaration file : declarations) {
                this.implFinder.visit(file, declaration);
            }
            if (this.implFinder.getClasses().size() > 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Find all the factory interfaces.
     *
     * @param declarations List of all the declarations
     * @return List of all the factory interfaces.
     */
    private List<ClassOrInterfaceDeclaration> findFactoryInterfaces(
            final List<ClassOrInterfaceDeclaration> declarations) {
        ArrayList<ClassOrInterfaceDeclaration> interfaces = new ArrayList<>();

        for (ClassOrInterfaceDeclaration declaration : declarations) {
            if (!declaration.isInterface()) {
                continue;
            }

            // Make sure this interface has return types that are other interfaces.
            boolean hasValidInterface = false;
            for (Node node : declaration.getChildNodes()) {
                if (!(node instanceof MethodDeclaration)) {
                    continue;
                }
                MethodDeclaration methodDeclaration = (MethodDeclaration) node;
                if (!(methodDeclaration.getType() instanceof ClassOrInterfaceType)) {
                    continue;
                }
                ClassOrInterfaceType type = (ClassOrInterfaceType) methodDeclaration.getType();
                ResolvedReferenceTypeDeclaration typeDeclaration =
                        ((ResolvedReferenceType) type.resolve()).getTypeDeclaration();

                //If the type is a interface
                if (!(typeDeclaration instanceof JavaParserInterfaceDeclaration)) {
                    continue;
                }

                ClassOrInterfaceDeclaration resolvedInterface =
                        ((JavaParserInterfaceDeclaration) typeDeclaration).getWrappedNode();
                if (resolvedInterface != null) {
                    hasValidInterface = true;
                    break;
                }
            }
            if (hasValidInterface) {
                interfaces.add(declaration);
            }
        }

        return interfaces;
    }
}