package nl.windesheim.codeparser.analyzers.abstractfactory.finders;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Find all the abstract class factories.
 */
public class AbstractFactoryFinder extends AbstractFinder {

    @Override
    public List<ClassOrInterfaceDeclaration> find(final List<ClassOrInterfaceDeclaration> declarations) {
        // Now that is done, lets try to find factories without interfaces. These are a bit simpler.
        List<ClassOrInterfaceDeclaration> classes = this.findFactoryAbstractClasses(declarations);
        this.log("I was able to find " + classes.size() + " of abstract factory classes.");

        List<ClassOrInterfaceDeclaration> implementations =
                this.findFactoryAbstractImplementations(classes, declarations);
        this.log("I was able to find " + implementations.size() + " "
                + "of abstract factory implementations.");

        return new ArrayList<>(this.findFactoryClasses(declarations, implementations));
    }

    /**
     * Find the implementations of all the given factory abstract classes.
     *
     * @param factoryAbstracts List of all the abstract factory classes.
     * @param declarations     List of all the declarations.
     * @return A list of the found implementations.
     */
    private List<ClassOrInterfaceDeclaration>
    findFactoryAbstractImplementations(final List<ClassOrInterfaceDeclaration> factoryAbstracts,
                                       final List<ClassOrInterfaceDeclaration> declarations) {
        ArrayList<ClassOrInterfaceDeclaration> implementations = new ArrayList<>();

        for (ClassOrInterfaceDeclaration factory : factoryAbstracts) {
            implementations.addAll(this.findImplementations(factory, declarations));
        }

        return implementations;
    }

    /**
     * Find all the abstract factory classes.
     *
     * @param declarations List of all the declarations.
     * @return A list of the abstract factory classes.
     */
    private List<ClassOrInterfaceDeclaration> findFactoryAbstractClasses(
            final List<ClassOrInterfaceDeclaration> declarations) {
        ArrayList<ClassOrInterfaceDeclaration> abstractClasses = new ArrayList<>();

        for (ClassOrInterfaceDeclaration declaration : declarations) {
            // Check if the declaration is abstract.
            boolean isAbstract = false;
            for (Modifier modifier : declaration.getModifiers()) {
                if (modifier.name().equals("ABSTRACT")) {
                    isAbstract = true;
                    break;
                }
            }
            if (!isAbstract) {
                continue;
            }

            abstractClasses.add(declaration);
        }

        return abstractClasses;
    }
}
