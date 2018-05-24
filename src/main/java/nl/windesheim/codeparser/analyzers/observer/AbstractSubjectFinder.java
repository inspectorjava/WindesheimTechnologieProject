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
        this.findSubscriptionMethods(declaration, eligibleCollections);

        // TODO Implement
        // Bevat een notify-methode, een methode waarin voor alle objecten in de collectie een bepaalde methode (update) wordt aangeroepen.

        // TODO Implement
        // Het is mogelijk dat het subject als een interface is gedefinieerd, in dat geval moeten de attach, detach en notify-methodes door het interface worden afgedwongen, en moeten deze op bovenstaande manier worden geïmplementeerd door de realisaties van de interface.
        // Zoek in dat geval, als 'shortcut', naar welke interface door deze klasse wordt geimplementeerd die deze methodes afdwingt. Dan zijn andere
        // klassen die deze interface realiseren en deze methodes implementeren ook een abstractsubject

    }

    private void findSubscriptionMethods (final ClassOrInterfaceDeclaration classDeclaration, final List<EligibleCollection> eligibleCollections) {
        // Vind alle methoden in de klasse die aangrijpen op een van de EligibleCollections, zij het door een add- of remove-methode
        // Find all methods operating on the found collections

        // For each method
        List<MethodDeclaration> methodDeclarations = classDeclaration.findAll(MethodDeclaration.class);
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            // Does it take a parameter of the type in an eligible collection?
                // No? Return
            NodeList<Parameter> parameters = methodDeclaration.getParameters();
            List<EligibleSubscriptionParameter> eligibleParameters = new ArrayList<>();

            for (Parameter parameter : parameters) {
                ResolvedType parameterType = parameter.getType().resolve();
                if (parameterType.isReferenceType()) {
                    ResolvedReferenceType parameterReferenceType = (ResolvedReferenceType) parameterType;
                    String qualifiedName = ((ResolvedReferenceType)parameterType).getQualifiedName();

                    EligibleSubscriptionParameter eligibleParameter = new EligibleSubscriptionParameter();
                    eligibleParameter.parameter = parameter;
                    eligibleParameter.resolvedReferenceType = parameterReferenceType;

                    for (EligibleCollection eligibleCollection : eligibleCollections) {
                        if (parameterReferenceType.getQualifiedName().equals(eligibleCollection.getReferType().getQualifiedName())) {
                            eligibleParameter.matchingCollections.add(eligibleCollection);
                        }
                    }

                    if (eligibleCollections.size() > 0) {
                        eligibleParameters.add(eligibleParameter);
                    }
                }
            }

            if (eligibleParameters.isEmpty()) {
                continue;
            }

            // Does it contain a method declaration with scope of one of the eligible collections?
                // No? Return
            List<MethodCallExpr> methodCalls = methodDeclaration.findAll(MethodCallExpr.class);
            for (MethodCallExpr methodCall : methodCalls) {
                Optional<Expression> optionalScope = methodCall.getScope();
                if (!optionalScope.isPresent()) {
                    continue;
                }

                Expression scopeExpression = optionalScope.get();

                if (!scopeExpression.isFieldAccessExpr()) {
                    continue;
                }

                FieldAccessExpr fieldAccessExpression = scopeExpression.asFieldAccessExpr();
                for (EligibleCollection eligibleCollection : eligibleCollections) {
                    // TODO Resolve scope?
                    if (fieldAccessExpression.getNameAsString().equals(eligibleCollection.getVariableDeclarator().getNameAsString())) {
                        // Does that method declaration operate on the add or remove method of the collection type?
                            // No? just keep looking
                        System.out.println("Accessing method on field " + eligibleCollection.getVariableDeclarator().getNameAsString());
                    }
                }
            }
        }
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

            ResolvedReferenceType referenceType = resolvedType.asReferenceType();

            // Try to determine if this is a collection
            String targetName = "java.util.Collection";
            ResolvedReferenceType parameterType = null;
            for (ResolvedReferenceType ancestor : referenceType.getAllAncestors()) {
                if (ancestor.getQualifiedName().equals(targetName)) {
                    // Determine the type that's stored in the collection
                    parameterType = this.determineParameterType(referenceType);
                    break;
                }
            }

            // If a parameter type has been found, this is an eligible collection
            if (parameterType != null) {
                for (VariableDeclarator variableDeclarator : field.getVariables()) {
                    EligibleCollection collection = new EligibleCollection(variableDeclarator, parameterType);
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

    private class EligibleSubscriptionParameter {
        private Parameter parameter;
        private ResolvedReferenceType resolvedReferenceType;
        private String parameterName;
        private List<EligibleCollection> matchingCollections;

        private EligibleSubscriptionParameter () {
            matchingCollections = new ArrayList<>();
        }
    }
}
