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
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.chainofresponsibility.EligibleCommonParentFinder;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;
import nl.windesheim.codeparser.patterns.AbstractFactory;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class AbstractFactoryAnalyzer extends PatternAnalyzer {

    private CombinedTypeSolver typeSolver;
    private ImplementationOrSuperclassFinder implFinder;

    public AbstractFactoryAnalyzer() {
        super();
    }


    @Override
    public List<IDesignPattern> analyze(List<CompilationUnit> files) {
        ArrayList<IDesignPattern> patterns = new ArrayList<>();

        if(files.size() == 0) {
            this.log("Got 0 files. Aborting.");
            return patterns;
        }

        this.typeSolver = getParent().getTypeSolver();
        this.implFinder = new ImplementationOrSuperclassFinder();

        ArrayList<ClassOrInterfaceDeclaration> declarations = this.findDeclarations(files);

        ArrayList<ClassOrInterfaceDeclaration> factoryClasses = new ArrayList<>();

        // If there are no interfaces, it is never possible to have an abstract factory pattern.
        // If there are interfaces, but they are not implemented. Ignore this pattern too.
        if(this.hasInterfaces(declarations) && this.interfacesAreImplemented(declarations)) {
            // These are all the factory interfaces
            ArrayList<ClassOrInterfaceDeclaration> factoryInterfaces = this.findFactoryInterfaces(declarations);
            this.log("I was able to find " + factoryInterfaces.size() + " of factory interfaces.");

            ArrayList<ClassOrInterfaceDeclaration> factoryImplementations = this.findFactoryImplementations(factoryInterfaces, declarations);
            this.log("I was able to find " + factoryImplementations.size() + " of factory implementations.");

            factoryClasses.addAll(this.findFactoryClasses(declarations, factoryImplementations));
            this.log("I was able to find " + factoryClasses.size() + " Factory classes.");
        }

        // Now that is done, lets try to find factories without interfaces. These are a bit simpler.
        ArrayList<ClassOrInterfaceDeclaration> factoryAbstractClasses = this.findFactoryAbstractClasses(declarations);
        this.log("I was able to find " + factoryAbstractClasses.size() + " of abstract factory classes.");

        ArrayList<ClassOrInterfaceDeclaration> factoryAbstractImplementations = this.findFactoryAbstractImplementations(factoryAbstractClasses, declarations);
        this.log("I was able to find " + factoryAbstractImplementations.size() + " of abstract factory implementations.");

        factoryClasses.addAll(this.findFactoryClasses(declarations, factoryAbstractImplementations));

        for(ClassOrInterfaceDeclaration factory : factoryClasses){
            AbstractFactory abstractFactory = new AbstractFactory();
            abstractFactory.setFactoryInterface(factory);
            patterns.add(abstractFactory);
        }

        return patterns;
    }

    private ArrayList<ClassOrInterfaceDeclaration> findFactoryClasses(ArrayList<ClassOrInterfaceDeclaration> declarations, ArrayList<ClassOrInterfaceDeclaration> factoryImplementations) {
        ArrayList<ClassOrInterfaceDeclaration> factoryClasses = new ArrayList<>();
        for(ClassOrInterfaceDeclaration declaration : declarations){
            ArrayList<MethodDeclaration> methodsInDeclaration = new ArrayList<>();
            methodsInDeclaration = this.findMethodDeclarations(declaration, methodsInDeclaration);

            for(MethodDeclaration method : methodsInDeclaration){
                for(ClassOrInterfaceDeclaration factoryImplementation : factoryImplementations){
                    MethodDeclaration methodDeclaration = (MethodDeclaration)method;
                    if(!(methodDeclaration.getType() instanceof ClassOrInterfaceType)) continue;
                    ClassOrInterfaceType type = (ClassOrInterfaceType) method.getType();
                    ResolvedReferenceTypeDeclaration typeDeclaration = ((ResolvedReferenceType) type.resolve()).getTypeDeclaration();

                    //If the type is a interface
                    if ((typeDeclaration instanceof JavaParserInterfaceDeclaration)) {
                        ClassOrInterfaceDeclaration resolvedInterface = ((JavaParserInterfaceDeclaration) typeDeclaration).getWrappedNode();
                        String declerationName = resolvedInterface.asClassOrInterfaceDeclaration().getNameAsString();
                        String interfaceName   = factoryImplementation.getImplementedTypes().get(0).getNameAsString();
                        if(declerationName.equals(interfaceName)){
                            if(!factoryClasses.contains(resolvedInterface.asClassOrInterfaceDeclaration())) {
                                factoryClasses.add(resolvedInterface.asClassOrInterfaceDeclaration());
                            }
                        }
                    }else if((typeDeclaration instanceof JavaParserClassDeclaration)){
                        ClassOrInterfaceDeclaration resolvedInterface = ((JavaParserClassDeclaration) typeDeclaration).getWrappedNode();
                        String declerationName = resolvedInterface.asClassOrInterfaceDeclaration().getNameAsString();
                        String interfaceName   = factoryImplementation.getExtendedTypes().get(0).getNameAsString();
                        if(declerationName.equals(interfaceName)){
                            if(!factoryClasses.contains(resolvedInterface.asClassOrInterfaceDeclaration())) {
                                factoryClasses.add(resolvedInterface.asClassOrInterfaceDeclaration());
                            }
                        }
                    }
                }
            }
        }
        return factoryClasses;
    }

    private ArrayList<MethodDeclaration> findMethodDeclarations(ClassOrInterfaceDeclaration declaration, ArrayList<MethodDeclaration> methods) {
        for(Node node : declaration.getChildNodes()){
            if(node instanceof MethodDeclaration){
                MethodDeclaration method = (MethodDeclaration)node;
                methods.add(method);
            }else if(node instanceof ClassOrInterfaceDeclaration){
                ClassOrInterfaceDeclaration classOrInterface = (ClassOrInterfaceDeclaration)node;
                this.findMethodDeclarations(classOrInterface, methods);
            }
        }
        return methods;
    }

    private ArrayList<ClassOrInterfaceDeclaration> findFactoryImplementations(ArrayList<ClassOrInterfaceDeclaration> factoryInterfaces, ArrayList<ClassOrInterfaceDeclaration> declerations) {
        ArrayList<ClassOrInterfaceDeclaration> factoryImplementations = new ArrayList<>();

        for(ClassOrInterfaceDeclaration factory : factoryInterfaces){
            factoryImplementations.addAll(this.findImplementations(factory, declerations));
        }

        return factoryImplementations;
    }

    private ArrayList<ClassOrInterfaceDeclaration> findFactoryAbstractImplementations(ArrayList<ClassOrInterfaceDeclaration> factoryAbstracts, ArrayList<ClassOrInterfaceDeclaration> declarations) {
        ArrayList<ClassOrInterfaceDeclaration> factoryAbstractImplementations = new ArrayList<>();

        for(ClassOrInterfaceDeclaration factory : factoryAbstracts){
            factoryAbstractImplementations.addAll(this.findImplementations(factory, declarations));
        }

        return factoryAbstractImplementations;
    }

    /**
     * A factory interface is an interface that has
     * @param declarations
     * @return
     */
    private ArrayList<ClassOrInterfaceDeclaration> findFactoryInterfaces(ArrayList<ClassOrInterfaceDeclaration> declarations) {
        ArrayList<ClassOrInterfaceDeclaration> interfaces = new ArrayList<>();

        for(ClassOrInterfaceDeclaration declaration : declarations){
            if(!declaration.isInterface()) continue;

            // Make sure this interface has return types that are other interfaces.
            boolean hasValidInterface = false;
            for(Node node : declaration.getChildNodes()){
                // @todo: make this a function
                if(!(node instanceof MethodDeclaration)) continue;
                MethodDeclaration methodDeclaration = (MethodDeclaration)node;
                if(!(methodDeclaration.getType() instanceof ClassOrInterfaceType)) continue;
                ClassOrInterfaceType type = (ClassOrInterfaceType) methodDeclaration.getType();
                ResolvedReferenceTypeDeclaration typeDeclaration = ((ResolvedReferenceType) type.resolve()).getTypeDeclaration();

                //If the type is a interface
                if (!(typeDeclaration instanceof JavaParserInterfaceDeclaration)) {
                    continue;
                }

                ClassOrInterfaceDeclaration resolvedInterface = ((JavaParserInterfaceDeclaration) typeDeclaration).getWrappedNode();
                if(resolvedInterface != null){
                    hasValidInterface = true;
                    break;
                }
            }
            if(hasValidInterface){
                interfaces.add(declaration);
            }
        }

        return interfaces;
    }

    private ArrayList<ClassOrInterfaceDeclaration> findFactoryAbstractClasses(ArrayList<ClassOrInterfaceDeclaration> declarations) {
        ArrayList<ClassOrInterfaceDeclaration> abstractClasses = new ArrayList<>();

        for(ClassOrInterfaceDeclaration declaration : declarations){
            // Check if the declaration is abstract.
            boolean isAbstract = false;
            for(Modifier modifier : declaration.getModifiers()){
                if(modifier.name().equals("ABSTRACT")){
                    isAbstract = true;
                    break;
                }
            }
            if(!isAbstract) continue;

            abstractClasses.add(declaration);
        }

        return abstractClasses;
    }

    private boolean interfacesAreImplemented(ArrayList<ClassOrInterfaceDeclaration> declarations) {
        for(ClassOrInterfaceDeclaration declaration : declarations){
            if(!declaration.isInterface()) continue;

            this.implFinder.reset();
            for(ClassOrInterfaceDeclaration file : declarations){
                this.implFinder.visit(file, declaration);
            }
            if(this.implFinder.getClasses().size() > 1) return true;
        }

        return false;
    }

    private boolean hasInterfaces(ArrayList<ClassOrInterfaceDeclaration> declarations) {
        for(ClassOrInterfaceDeclaration declaration : declarations){
            if(declaration.isInterface()) return true;
        }
        return false;
    }

    private ArrayList<ClassOrInterfaceDeclaration> findDeclarations(List<CompilationUnit> files) {
        ArrayList<ClassOrInterfaceDeclaration> declarations = new ArrayList<>();
        for(CompilationUnit compilationUnit : files){
            for(Node node : compilationUnit.getChildNodes()){
                if(node instanceof ClassOrInterfaceDeclaration){
                    declarations.add((ClassOrInterfaceDeclaration) node);
                }
            }
        }
        return declarations;
    }

    private Collection<? extends ClassOrInterfaceDeclaration> findImplementations(ClassOrInterfaceDeclaration factory, ArrayList<ClassOrInterfaceDeclaration> declerations) {
        this.implFinder.reset();
        for(ClassOrInterfaceDeclaration allClass : declerations){
            this.implFinder.visit(allClass, factory);
        }
        return this.implFinder.getClasses();
    }

    private void log(String message) {
        System.out.println(message);
    }
}
