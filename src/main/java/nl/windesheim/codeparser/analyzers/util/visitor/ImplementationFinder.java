package nl.windesheim.codeparser.analyzers.util.visitor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Searches for classes which implement the given interface.
 */
public class ImplementationFinder extends VoidVisitorAdapter<ClassOrInterfaceType>{

    /**
     * The classes which implement the given interface
     */
    private ArrayList<ClassOrInterfaceDeclaration> classes;

    /**
     * Make a new EligibleStrategyContextFinder
     */
    public ImplementationFinder(){
        classes = new ArrayList<>();
    }

    /**
     * @return A of classes which implement the given interface
     */
    public ArrayList<ClassOrInterfaceDeclaration> getClasses(){
        return classes;
    }

    /**
     * Resets the list of classes
     */
    public void reset(){
        classes = new ArrayList<>();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classToCheck, ClassOrInterfaceType interfaceType) {
        super.visit(classToCheck, interfaceType);

        for (ClassOrInterfaceType implementedInterface : classToCheck.getImplementedTypes()){
            if (implementedInterface.equals(interfaceType)){
               classes.add(classToCheck);
            }
        }
    }
}
