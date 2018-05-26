package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import java.util.*;

/**
 * Visitor which finds all classes which can be 'context' classes.
 */
public class AbstractSubjectFinder
        extends VoidVisitorAdapter<Void> {

    private SymbolResolver symbolResolver;

    private TypeSolver typeSolver;

    private ClassOrInterfaceDeclaration abstractSubject;

    private List<ClassOrInterfaceDeclaration> observers;

    /**
     * Make a new AbstractSubjectFinder.
     */
    public AbstractSubjectFinder(final TypeSolver typeSolver) {
        super();
        this.typeSolver = typeSolver;
        this.symbolResolver = new JavaSymbolSolver(typeSolver);
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration declaration, Void arg) {
        // An AbstractSubject is an (abstract) class with the following characteristics

        // Contains a collection of objects (of a reference type)
        List<EligibleCollection> eligibleCollections = this.findEligibleCollections(declaration);
        if (eligibleCollections.isEmpty()) {
            return;
        }

        // Contains attach- and detach methods
        SubscriptionMethodFinder subscriptionMethodFinder = new SubscriptionMethodFinder(typeSolver, eligibleCollections);
        subscriptionMethodFinder.findSubscriptionMethods(declaration, eligibleCollections);

        for (EligibleCollection collection : eligibleCollections) {
            System.out.print("Collection " + collection.getVariableDeclarator().getNameAsString() + " ");
            System.out.println(collection.isObserverCollection() ? "Ja!" : "Nee");
            System.out.println();
        }

        // TODO Implement
        // Bevat een notify-methode, een methode waarin voor alle objecten in de collectie een bepaalde methode (update) wordt aangeroepen.

        // TODO Implement
        // Het is mogelijk dat het subject als een interface is gedefinieerd, in dat geval moeten de attach, detach en notify-methodes door het interface worden afgedwongen, en moeten deze op bovenstaande manier worden geïmplementeerd door de realisaties van de interface.
        // Zoek in dat geval, als 'shortcut', naar welke interface door deze klasse wordt geimplementeerd die deze methodes afdwingt. Dan zijn andere
        // klassen die deze interface realiseren en deze methodes implementeren ook een abstractsubject

    }





    private List<EligibleCollection> findEligibleCollections (final ClassOrInterfaceDeclaration classDeclaration) {
        List<EligibleCollection> collections = new ArrayList<>();

        for (FieldDeclaration field : classDeclaration.getFields()) {
            Type variableType = field.getVariable(0).getType();
            ResolvedType resolvedType = JavaParserFacade.get(typeSolver).convertToUsage(variableType, field);

            // A collection is always a reference type
            if (!resolvedType.isReferenceType()) {
                continue;
            }

            ResolvedReferenceType fieldType = resolvedType.asReferenceType();

            // Try to determine if this is a collection
            String targetName = "java.util.Collection";
            ResolvedReferenceType parameterType = null;
            for (ResolvedReferenceType ancestor : fieldType.getAllAncestors()) {
                if (ancestor.getQualifiedName().equals(targetName)) {
                    // Determine the type that's stored in the collection
                    parameterType = this.determineParameterType(fieldType);
                    break;
                }
            }

            // If a parameter type has been found, this is an eligible collection
            if (parameterType != null) {
                for (VariableDeclarator variableDeclarator : field.getVariables()) {
                    EligibleCollection collection = new EligibleCollection(variableDeclarator, fieldType, parameterType);
                    collections.add(collection);
                }
            }
        }

        return collections;
    }

    private ResolvedReferenceType determineParameterType (final ResolvedReferenceType collectionType) {
        // Haal het type op waarmee de lijst gevuld is. Als aan de andere criteria voor een abstract subject
        // wordt voldaan is dit type de mogelijke observer
        List<ResolvedType> parameters = collectionType.typeParametersValues();

        if (parameters.size() > 0) {
            ResolvedType parameter = parameters.get(parameters.size() - 1);
            if (parameter.isReferenceType()) {
                return parameter.asReferenceType();
            }
        }

        return null;
    }
}
