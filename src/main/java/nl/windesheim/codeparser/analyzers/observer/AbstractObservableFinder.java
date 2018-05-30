package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.componentfinders.NotificationMethodFinder;
import nl.windesheim.codeparser.analyzers.observer.componentfinders.SubscriptionMethodFinder;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObservable;
import nl.windesheim.codeparser.analyzers.observer.components.ObserverCollection;

import java.util.*;

/**
 * Visitor which finds all classes which can be 'context' classes.
 */
public class AbstractObservableFinder
        extends VoidVisitorAdapter<Void> {

    private TypeSolver typeSolver;

    private List<AbstractObservable> abstractObservables;

    /**
     * Make a new AbstractObservableFinder.
     */
    public AbstractObservableFinder(final TypeSolver typeSolver) {
        super();
        this.typeSolver = typeSolver;
        this.abstractObservables = new ArrayList<>();
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        if (!classDeclaration.isInterface()) {
            // Contains a collection of objects (of a reference type)
            List<ObserverCollection> eligibleCollections = this.findEligibleCollections(classDeclaration);
            if (eligibleCollections.isEmpty()) {
                return;
            }

            // Check if the class contains attach-, detach- and notify methods
            SubscriptionMethodFinder subscriptionMethodFinder = new SubscriptionMethodFinder(typeSolver, eligibleCollections);
            NotificationMethodFinder notificationMethodFinder = new NotificationMethodFinder(typeSolver, eligibleCollections);


            List<MethodDeclaration> methodDeclarations = classDeclaration.findAll(MethodDeclaration.class);
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                EnumSet<Modifier> modifiers = methodDeclaration.getModifiers();
                if (!modifiers.contains(Modifier.PRIVATE)) {
                    subscriptionMethodFinder.determine(methodDeclaration);
                    notificationMethodFinder.determine(methodDeclaration);
                }
            }

            List<ObserverCollection> observerCollections = new ArrayList<>();
            for (ObserverCollection collection : eligibleCollections) {
                if (collection.isObserverCollection()) {
                    observerCollections.add(collection);
                }
            }

            // If an abstract observable has been found, store info
            if (!observerCollections.isEmpty()) {
                abstractObservables.add(new AbstractObservable(classDeclaration, observerCollections));
            }
        }
    }

    public List<AbstractObservable> getAbstractObservables () {
        return abstractObservables;
    }

    private List<ObserverCollection> findEligibleCollections (final ClassOrInterfaceDeclaration classDeclaration) {
        List<ObserverCollection> collections = new ArrayList<>();

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
                    ObserverCollection collection = new ObserverCollection(variableDeclarator, fieldType, parameterType);
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
