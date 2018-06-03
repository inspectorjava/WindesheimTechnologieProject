package nl.windesheim.codeparser.analyzers.abstractfactory;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;
import nl.windesheim.codeparser.patterns.AbstractFactory;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The full class to analyze compilation units to check if an (Abstract) Factory pattern is used.
 */
public class AbstractFactoryAnalyzer extends PatternAnalyzer {

    /**
     * The implementation finder.
     */
    private ImplementationOrSuperclassFinder implFinder;

    /**
     * The constructor.
     */
    public AbstractFactoryAnalyzer() {
        super();
    }


    @Override
    public List<IDesignPattern> analyze(final List<CompilationUnit> files) {
        ArrayList<IDesignPattern> patterns = new ArrayList<>();

        if (files.size() == 0) {
            this.log("Got 0 files. Aborting.");
            return patterns;
        }

        this.implFinder = new ImplementationOrSuperclassFinder();

        ArrayList<ClassOrInterfaceDeclaration> declarations = this.findDeclarations(files);

        ArrayList<ClassOrInterfaceDeclaration> factoryClasses = new ArrayList<>();

        // If there are no interfaces, it is never possible to have an abstract factory pattern.
        // If there are interfaces, but they are not implemented. Ignore this pattern too.
        if (this.hasInterfaces(declarations) && this.interfacesAreImplemented(declarations)) {
            // These are all the factory interfaces
            ArrayList<ClassOrInterfaceDeclaration> factoryInterfaces = this.findFactoryInterfaces(declarations);
            this.log("I was able to find " + factoryInterfaces.size() + " of factory interfaces.");

            ArrayList<ClassOrInterfaceDeclaration> factoryImplementations =
                    this.findFactoryImplementations(factoryInterfaces, declarations);
            this.log("I was able to find " + factoryImplementations.size() + " of factory implementations.");

            factoryClasses.addAll(this.findFactoryClasses(declarations, factoryImplementations));
            this.log("I was able to find " + factoryClasses.size() + " Factory classes.");
        }

        // Now that is done, lets try to find factories without interfaces. These are a bit simpler.
        ArrayList<ClassOrInterfaceDeclaration> factoryAbstractClasses = this.findFactoryAbstractClasses(declarations);
        this.log("I was able to find " + factoryAbstractClasses.size() + " of abstract factory classes.");

        ArrayList<ClassOrInterfaceDeclaration> factoryAbstractImplementations =
                this.findFactoryAbstractImplementations(factoryAbstractClasses, declarations);
        this.log("I was able to find " + factoryAbstractImplementations.size() + " "
                + "of abstract factory implementations.");

        factoryClasses.addAll(this.findFactoryClasses(declarations, factoryAbstractImplementations));

        for (ClassOrInterfaceDeclaration factory : factoryClasses) {
            AbstractFactory abstractFactory = new AbstractFactory();
            abstractFactory.setFactoryInterface(factory);
            patterns.add(abstractFactory);
        }

        return patterns;
    }

    /**
     * Find all the factory classes. These are all the classes that implement or extend the factory classes.
     *
     * @param declarations           List of all the file declarations.
     * @param factoryImplementations List of all the abstract factory pattern implementations
     * @return A list with all the factory classes.
     */
    private ArrayList<ClassOrInterfaceDeclaration>
    findFactoryClasses(final ArrayList<ClassOrInterfaceDeclaration> declarations,
                       final ArrayList<ClassOrInterfaceDeclaration> factoryImplementations) {
        ArrayList<ClassOrInterfaceDeclaration> factoryClasses = new ArrayList<>();
        for (ClassOrInterfaceDeclaration declaration : declarations) {
            ArrayList<MethodDeclaration> methodsInDeclaration = new ArrayList<>();
            methodsInDeclaration = this.findMethodDeclarations(declaration, methodsInDeclaration);

            for (MethodDeclaration method : methodsInDeclaration) {
                for (ClassOrInterfaceDeclaration factoryImplementation : factoryImplementations) {
                    MethodDeclaration methodDeclaration = (MethodDeclaration) method;
                    if (!(methodDeclaration.getType() instanceof ClassOrInterfaceType)) {
                        continue;
                    }
                    ClassOrInterfaceType type = (ClassOrInterfaceType) method.getType();
                    ResolvedReferenceTypeDeclaration typeDeclaration =
                            ((ResolvedReferenceType) type.resolve()).getTypeDeclaration();

                    //If the type is a interface
                    if ((typeDeclaration instanceof JavaParserInterfaceDeclaration)) {
                        ClassOrInterfaceDeclaration resolvedInterface =
                                ((JavaParserInterfaceDeclaration) typeDeclaration).getWrappedNode();
                        String declerationName = resolvedInterface.asClassOrInterfaceDeclaration().getNameAsString();
                        String interfaceName = factoryImplementation.getImplementedTypes().get(0).getNameAsString();
                        if (declerationName.equals(interfaceName)) {
                            if (!factoryClasses.contains(resolvedInterface.asClassOrInterfaceDeclaration())) {
                                factoryClasses.add(resolvedInterface.asClassOrInterfaceDeclaration());
                            }
                        }
                    } else if ((typeDeclaration instanceof JavaParserClassDeclaration)) {
                        ClassOrInterfaceDeclaration resolvedInterface =
                                ((JavaParserClassDeclaration) typeDeclaration).getWrappedNode();
                        String declerationName = resolvedInterface.asClassOrInterfaceDeclaration().getNameAsString();
                        String interfaceName = factoryImplementation.getExtendedTypes().get(0).getNameAsString();
                        if (declerationName.equals(interfaceName)) {
                            if (!factoryClasses.contains(resolvedInterface.asClassOrInterfaceDeclaration())) {
                                factoryClasses.add(resolvedInterface.asClassOrInterfaceDeclaration());
                            }
                        }
                    }
                }
            }
        }
        return factoryClasses;
    }

    /**
     * Recursively find all the method declarations inside a class.
     *
     * @param declaration The declaration to search in.
     * @param methods     A list of all the found declarations so far.
     * @return A list of all the found declarations.
     */
    private ArrayList<MethodDeclaration> findMethodDeclarations(final ClassOrInterfaceDeclaration declaration,
                                                                final ArrayList<MethodDeclaration> methods) {
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
     * Find the implementations of all the given factory interfaces.
     *
     * @param factoryInterfaces A list of the found factory interfaces.
     * @param declarations      A list of all the found declarations.
     * @return A list of all the found factory implementations.
     */
    private ArrayList<ClassOrInterfaceDeclaration>
    findFactoryImplementations(final ArrayList<ClassOrInterfaceDeclaration> factoryInterfaces,
                               final ArrayList<ClassOrInterfaceDeclaration> declarations) {
        ArrayList<ClassOrInterfaceDeclaration> factoryImplementations = new ArrayList<>();

        for (ClassOrInterfaceDeclaration factory : factoryInterfaces) {
            factoryImplementations.addAll(this.findImplementations(factory, declarations));
        }

        return factoryImplementations;
    }

    /**
     * Find the implementations of all the given factory abstract classes.
     *
     * @param factoryAbstracts List of all the abstract factory classes.
     * @param declarations     List of all the declarations.
     * @return A list of the found implementations.
     */
    private ArrayList<ClassOrInterfaceDeclaration>
    findFactoryAbstractImplementations(final ArrayList<ClassOrInterfaceDeclaration> factoryAbstracts,
                                       final ArrayList<ClassOrInterfaceDeclaration> declarations) {
        ArrayList<ClassOrInterfaceDeclaration> factoryAbstractImplementations = new ArrayList<>();

        for (ClassOrInterfaceDeclaration factory : factoryAbstracts) {
            factoryAbstractImplementations.addAll(this.findImplementations(factory, declarations));
        }

        return factoryAbstractImplementations;
    }

    /**
     * Find all the factory interfaces.
     *
     * @param declarations List of all the declarations
     * @return List of all the factory interfaces.
     */
    private ArrayList<ClassOrInterfaceDeclaration> findFactoryInterfaces(
            final ArrayList<ClassOrInterfaceDeclaration> declarations) {
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

    /**
     * Find all the abstract factory classes.
     *
     * @param declarations List of all the declarations.
     * @return A list of the abstract factory classes.
     */
    private ArrayList<ClassOrInterfaceDeclaration> findFactoryAbstractClasses(
            final ArrayList<ClassOrInterfaceDeclaration> declarations) {
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

    /**
     * Check if the interfaces are at least implemented in 1 or more files.
     *
     * @param declarations List of the declarations
     * @return A boolean if the interface(s) is/are implemented.
     */
    private boolean interfacesAreImplemented(final ArrayList<ClassOrInterfaceDeclaration> declarations) {
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
     * Check if at least 1 of the declarations is an interface.
     *
     * @param declarations List of the declarations
     * @return boolean if at least 1 declaration is an interface.
     */
    private boolean hasInterfaces(final ArrayList<ClassOrInterfaceDeclaration> declarations) {
        for (ClassOrInterfaceDeclaration declaration : declarations) {
            if (declaration.isInterface()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find all the ClassOrInterface declarations based on all the compilation units.
     *
     * @param files List of all the compilation units.
     * @return List of all the ClassOrInterface declarations
     */
    private ArrayList<ClassOrInterfaceDeclaration> findDeclarations(final List<CompilationUnit> files) {
        ArrayList<ClassOrInterfaceDeclaration> declarations = new ArrayList<>();
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
     * Find implementations of a ClassOrInterfaceDeclaration file.
     *
     * @param factory      The Class or interface we want to check
     * @param declarations List of all the declarations
     * @return the implementations of the given declaration.
     */
    private Collection<? extends ClassOrInterfaceDeclaration>
    findImplementations(final ClassOrInterfaceDeclaration factory,
                        final ArrayList<ClassOrInterfaceDeclaration> declarations) {
        this.implFinder.reset();
        for (ClassOrInterfaceDeclaration allClass : declarations) {
            this.implFinder.visit(allClass, factory);
        }
        return this.implFinder.getClasses();
    }

    /**
     * Simple log helper to output some text.
     *
     * @param message the message to print.
     */
    private void log(final String message) {
        System.out.println(message);
    }
}
