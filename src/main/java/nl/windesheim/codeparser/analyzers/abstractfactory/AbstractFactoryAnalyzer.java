package nl.windesheim.codeparser.analyzers.abstractfactory;

import com.github.javaparser.ast.CompilationUnit;
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
import java.util.HashMap;
import java.util.List;

public class AbstractFactoryAnalyzer extends PatternAnalyzer {

    private CombinedTypeSolver typeSolver;
    private ImplementationOrSuperclassFinder implFinder;
    private EligibleCommonParentFinder parentFinder;

    public AbstractFactoryAnalyzer() {
        super();
        parentFinder    = new EligibleCommonParentFinder();
    }

    @Override
    public List<IDesignPattern> analyze(List<CompilationUnit> files) {
        ArrayList<IDesignPattern> patterns = new ArrayList<>();

        if(files.size() == 0) return patterns;

        this.typeSolver = getParent().getTypeSolver();
        this.implFinder = new ImplementationOrSuperclassFinder(typeSolver);

        ArrayList<ClassOrInterfaceDeclaration> declarations = this.findDeclarations(files);

        // If there are no interfaces, it is never possible to have an abstract factory pattern.
        if(!this.hasInterfaces(declarations)) return patterns;

        // If there are interfaces, but they are not implemented. Ignore this pattern too.
        if(!this.interfacesAreImplemented(declarations)) return patterns;

        // These are all the factory interfaces
        ArrayList<ClassOrInterfaceDeclaration> factoryInterfaces = this.findFactoryInterfaces(declarations);

        ArrayList<ClassOrInterfaceDeclaration> factoryImplementations = this.findFactoryImplementations(factoryInterfaces, declarations);

        ArrayList<ClassOrInterfaceDeclaration> factoryClasses = this.findFactoryClasses(declarations, factoryImplementations);

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
                    if(!typeDeclaration.getName().equals("KingdomFactory")) continue;
                    //If the type is a interface
                    if (!(typeDeclaration instanceof JavaParserInterfaceDeclaration)) {
                        continue;
                    }

                    ClassOrInterfaceDeclaration resolvedInterface = ((JavaParserInterfaceDeclaration) typeDeclaration).getWrappedNode();
                    String declerationName = resolvedInterface.asClassOrInterfaceDeclaration().getNameAsString();
                    String interfaceName   = factoryImplementation.getImplementedTypes().get(0).getNameAsString();
                    if(declerationName.equals(interfaceName)){
                        if(!factoryClasses.contains(resolvedInterface.asClassOrInterfaceDeclaration())) {
                            factoryClasses.add(resolvedInterface.asClassOrInterfaceDeclaration());
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
            this.implFinder.reset();
            for(ClassOrInterfaceDeclaration allClass : declerations){
                this.implFinder.visit(allClass, factory);
            }
            factoryImplementations.addAll(this.implFinder.getClasses());
        }

        return factoryImplementations;
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
}
