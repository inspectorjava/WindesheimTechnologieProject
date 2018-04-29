package nl.windesheim.codeparser.analyzers.strategy;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import javafx.util.Pair;
import nl.windesheim.codeparser.analyzers.util.visitor.SetterFinder;

import java.util.ArrayList;

/**
 * Visitor which finds all classes which can be 'context' classes.
 */
public class EligibleStrategyContextFinder
        extends VoidVisitorAdapter<CombinedTypeSolver> {

    /**
     * The context, interface pairs found in the last visit
     */
    private ArrayList<Pair<VariableDeclarator, ClassOrInterfaceDeclaration>> classes;

    /**
     * A visitor which is used to tell if there exists a setter for a field
     */
    private SetterFinder setterFinder;

    /**
     * Make a new EligibleStrategyContextFinder
     */
    EligibleStrategyContextFinder(){
        classes = new ArrayList<>();
        setterFinder = new SetterFinder();
    }

    /**
     * @return A list of context, interface pairs which were found in the last visit
     */
    public ArrayList<Pair<VariableDeclarator, ClassOrInterfaceDeclaration>> getClasses(){
        return classes;
    }

    /**
     * Reset the list of classes
     */
    public void reset(){
        classes = new ArrayList<>();
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration declaration, final CombinedTypeSolver typeSolver) {
        //If this is a interface don't process further
        if (declaration.isInterface()){
            return;
        }

        //Get all fields in the class
        for (FieldDeclaration field : declaration.getFields()){

            //For every variable in a field deceleration
            for (VariableDeclarator variable : field.getVariables()) {
                //Get the type of the field
                Type variableType = variable.getType();

                //Try to resolve the type
                ResolvedType resolvedType;
                try {
                    resolvedType = JavaParserFacade.get(typeSolver).convertToUsage(variableType);
                } catch (UnsolvedSymbolException e) {
                    System.err.println("Can't resolve symbol: " + variableType.asString() + ", can be caused by missing" +
                            " dependencies, invalid java code or selection of invalid source root");
                    continue;
                }

                //if the type is a reference, not a primitive type
                if (!(resolvedType instanceof ResolvedReferenceType)) {
                    continue;
                }

                ResolvedReferenceTypeDeclaration typeDecleration
                        = ((ResolvedReferenceType) resolvedType).getTypeDeclaration();

                //If the type is a interface
                if (!(typeDecleration instanceof JavaParserInterfaceDeclaration)) {
                    continue;
                }

                ClassOrInterfaceDeclaration resolvedInterface =
                        ((JavaParserInterfaceDeclaration) typeDecleration).getWrappedNode();


                //If the interface has at least one method it is eligible as strategy interface
                if (resolvedInterface.getMethods().size() == 0) {
                    continue;
                }

                //Check if there is a setter for the field
                boolean hasSetter = setterFinder.visit(declaration, variable) != null;

                if (hasSetter) {
                    Pair<VariableDeclarator, ClassOrInterfaceDeclaration> newPair =
                            new Pair<>(variable, resolvedInterface);
                    classes.add(newPair);
                }
            }
        }
    }
}
