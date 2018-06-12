package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.componentfinders.NotificationMethodFinder;
import nl.windesheim.codeparser.analyzers.observer.componentfinders.SubscriptionMethodFinder;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObservable;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;
import nl.windesheim.codeparser.analyzers.observer.components.ObserverCollection;
import nl.windesheim.codeparser.analyzers.util.ErrorLog;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Visitor which finds all classes which can be an 'abstract observable'.
 */
public class AbstractObservableFinder
        extends VoidVisitorAdapter<Void> {

    /**
     * A tool which resolves relations between AST nodes.
     */
    private final TypeSolver typeSolver;

    /**
     * A list of potential observer patterns.
     */
    private final List<EligibleObserverPattern> observerPatterns;

    /**
     * AbstractObservableFinder constructor.
     *
     * @param typeSolver A TypeSolver which can be used by this class
     */
    public AbstractObservableFinder(final TypeSolver typeSolver) {
        super();
        this.typeSolver = typeSolver;
        this.observerPatterns = new ArrayList<>();
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration classDeclaration, final Void arg) {
        if (!classDeclaration.isInterface()) {
            // Contains a collection of objects (of a reference type)
            try {
                ResolvedReferenceTypeDeclaration classTypeDecl = classDeclaration.resolve();

                List<ObserverCollection> eligibleCols = this.findEligibleCollections(classDeclaration, classTypeDecl);
                if (eligibleCols.isEmpty()) {
                    return;
                }

                // Check if the class contains attach-, detach- and notify methods
                SubscriptionMethodFinder subscriptFinder =
                        new SubscriptionMethodFinder(typeSolver, eligibleCols);
                NotificationMethodFinder notifyFinder =
                        new NotificationMethodFinder(typeSolver, eligibleCols);

                List<MethodDeclaration> methods = classDeclaration.findAll(MethodDeclaration.class);
                for (MethodDeclaration method : methods) {
                    EnumSet<Modifier> modifiers = method.getModifiers();
                    if (!modifiers.contains(Modifier.PRIVATE)) {
                        subscriptFinder.determine(method);
                        notifyFinder.determine(method);
                    }
                }

                List<ObserverCollection> observerCols = new ArrayList<>();
                for (ObserverCollection eligibleCol : eligibleCols) {
                    if (eligibleCol.isObserverCollection()) {
                        observerCols.add(eligibleCol);
                    }
                }

                // If an abstract observable has been found, store info
                if (!observerCols.isEmpty()) {
                    AbstractObservable abstObservable =
                            new AbstractObservable(classDeclaration, classDeclaration.resolve(), observerCols);
                    EligibleObserverPattern observerPattern = new EligibleObserverPattern();
                    observerPattern.setAbstractObservable(abstObservable);
                    observerPatterns.add(observerPattern);
                }
            } catch (UnsolvedSymbolException ex) {
                ErrorLog.getInstance().addError(ex);
            }
        }
    }

    /**
     * @return A list of potentially detected observer patterns
     */
    public List<EligibleObserverPattern> getObserverPatterns() {
        return observerPatterns;
    }

    /**
     * Find object properties which may contain a collection of Observers, which may indicate that the given
     * class is an AbstractObservable.
     *
     * A possible Observer collection adheres to the following criteria:
     * - It is an object property
     * - It is a realization of a Java Collection
     * - It is parametrized with a reference type
     * -- Which not the same type as itself or an ancestor
     *
     * @param classDeclaration The class to find the eligible collection in
     * @return A list of object properties which fit the criteria for being an Observer collection
     */
    // TODO Mag naar eigen klasse?
    private List<ObserverCollection> findEligibleCollections(
            final ClassOrInterfaceDeclaration classDeclaration,
            final ResolvedReferenceTypeDeclaration classTypeDecl
    ) {
        List<ObserverCollection> collections = new ArrayList<>();

        for (FieldDeclaration field : classDeclaration.getFields()) {
            try {
                Type variableType = field.getVariable(0).getType();
                ResolvedType resolvedType = variableType.resolve();

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

                // Determine if the parameter is not of the same type as the class itself
                // or an ancestor of the class
                if (parameterType == null || isTypeEqualToSelf(parameterType, classTypeDecl)) {
                    continue;
                }

                // If a parameter type has been found, this is an eligible collection
                for (VariableDeclarator variable : field.getVariables()) {
                    ObserverCollection collection =
                            new ObserverCollection(variable, fieldType, parameterType);
                    collections.add(collection);
                }
            } catch (UnsolvedSymbolException ex) {
                // Right now, ignore this. At a later stage, add error reporting
                ErrorLog.getInstance().addError(ex);
            }
        }

        return collections;
    }

    private boolean isTypeEqualToSelf (final ResolvedReferenceType parameterType,
                                       final ResolvedReferenceTypeDeclaration classTypeDecl
    ) {
        // TODO Make partial pattern instead?
        if (parameterType.getTypeDeclaration().equals(classTypeDecl)) {
            return true;
        }

        List<ResolvedReferenceType> classAncestors = classTypeDecl.getAncestors();

        if (classAncestors.contains(parameterType)) {
            return true;
        }

        return false;
    }

    /**
     * Determines the parameter type of a generic collection.
     *
     * @param collectionType The collection type
     * @return The type of the parameter
     */
    private ResolvedReferenceType determineParameterType(final ResolvedReferenceType collectionType) {
        List<ResolvedType> parameters = collectionType.typeParametersValues();

        if (!parameters.isEmpty()) {
            ResolvedType parameter = parameters.get(parameters.size() - 1);
            if (parameter.isReferenceType()) {
                return parameter.asReferenceType();
            }
        }

        return null;
    }
}
