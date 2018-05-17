package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import javafx.util.Pair;
import nl.windesheim.codeparser.analyzers.util.visitor.SetterFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor which finds all classes which can be 'context' classes.
 */
public class AbstractSubjectFinder
        extends VoidVisitorAdapter<CombinedTypeSolver> {

    /**
     * Make a new EligibleStrategyContextFinder.
     */
    public AbstractSubjectFinder() {
        super();
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration declaration, final CombinedTypeSolver typeSolver) {
        //  Een Subject is een (abstracte) klasse met de volgende kenmerken:
        //  Bevat een collectie van objecten
        //  Bevat een methode om objecten aan deze collectie toe te voegen (attach)
        //  Bevat een methode om objecten uit deze collectie te verwijderen (detach)
        //  Bevat een notify-methode, een methode waarin voor alle objecten in de collectie een bepaalde methode (update) wordt aangeroepen.
        //  Het is mogelijk dat het subject als een interface is gedefinieerd, in dat geval moeten de attach, detach en notify-methodes door het interface worden afgedwongen, en moeten deze op bovenstaande manier worden geïmplementeerd door de realisaties van de interface.

        for (FieldDeclaration field : declaration.getFields()) {
            // Try to determine if this is a collection
            for (VariableDeclarator variable : field.getVariables()) {
                Type variableType = variable.getType();

                ResolvedType resolvedType = JavaParserFacade.get(typeSolver).convertToUsage(variableType);
                String name = resolvedType.asReferenceType().getQualifiedName();
                System.out.println(name);
            }
        }
    }
}
