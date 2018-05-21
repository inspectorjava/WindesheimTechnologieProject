package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import javafx.util.Pair;
import javassist.compiler.ast.MethodDecl;
import javassist.compiler.ast.Symbol;
import javassist.expr.MethodCall;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.util.visitor.SetterFinder;

import javax.swing.text.html.Option;
import java.util.*;

/**
 * Visitor which finds all classes which can be 'context' classes.
 */
public class AbstractSubjectFinder
        extends VoidVisitorAdapter<Void> {

    private SymbolResolver symbolResolver;

    private TypeSolver typeSolver;

    private Set<EligibleCollection> eligibleCollections;

    private ClassOrInterfaceDeclaration abstractSubject;

    private List<ClassOrInterfaceDeclaration> observers;

    /**
     * Make a new AbstractSubjectFinder.
     */
    public AbstractSubjectFinder(final TypeSolver typeSolver) {
        super();
        this.typeSolver = typeSolver;
        this.symbolResolver = new JavaSymbolSolver(typeSolver);
        eligibleCollections = new HashSet<>();
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration declaration, Void arg) {
        // An AbstractSubject is an (abstract) class with the following characteristics

        // Contains a collection of objects (of a reference type)
        eligibleCollections = this.findEligibleCollections(declaration);

        if (eligibleCollections.isEmpty()) {
            return;
        }

        // Contains attach- and detach methods
        this.findSubscriptionMethods(declaration);

        // TODO Implement
        // Bevat een notify-methode, een methode waarin voor alle objecten in de collectie een bepaalde methode (update) wordt aangeroepen.

        // TODO Implement
        // Het is mogelijk dat het subject als een interface is gedefinieerd, in dat geval moeten de attach, detach en notify-methodes door het interface worden afgedwongen, en moeten deze op bovenstaande manier worden geïmplementeerd door de realisaties van de interface.
        // Zoek in dat geval, als 'shortcut', naar welke interface door deze klasse wordt geimplementeerd die deze methodes afdwingt. Dan zijn andere
        // klassen die deze interface realiseren en deze methodes implementeren ook een abstractsubject

    }

    private void findSubscriptionMethods (final ClassOrInterfaceDeclaration classDeclaration) {
        // Vind alle methoden in de klasse die ingrijpen op de gevonden collecties, en de verwachte
        // parameter (van het observertype) meegeven aan een add, remove, addAll of removeAll-functie op de
        // collectie
        Set<String> observerTypes = new HashSet<>();
        for (EligibleCollection eligibleCollection : eligibleCollections) {
            observerTypes.add(eligibleCollection.getReferType().getQualifiedName());
        }

        // Find all methods operating on the found collections
        for (MethodDeclaration methodDeclaration : classDeclaration.getMethods()) {
            // Check if the method actually has any parameters with one of the observer types
            NodeList<Parameter> parameters = methodDeclaration.getParameters();
            JavaParserFacade javaParserFacade = JavaParserFacade.get(typeSolver);

            // TODO Change to Map<Parameter, ResolvedReferenceType>?
            Map<Parameter, String> parametersOfInterest = new HashMap<>();
            for (Parameter parameter : parameters) {
                ResolvedType parameterType = javaParserFacade.convertToUsage(parameter.getType(), parameter);
                if (!parameterType.isReferenceType()) {
                    continue;
                }

                ResolvedReferenceType parameterReferenceType = parameterType.asReferenceType();
                if (observerTypes.contains(parameterReferenceType.getQualifiedName())) {
                    parametersOfInterest.put(parameter, parameterReferenceType.getQualifiedName());
                }
            }

            // If there are no parameters with an observer type given, this cannot
            // be the method we're looking for
            if (parametersOfInterest.isEmpty()) {
                continue;
            }

            // Check if the method contains any method calls that operate on one of the collections
            List<MethodCallExpr> methodCalls = methodDeclaration.findAll(MethodCallExpr.class);
            for (MethodCallExpr methodCall : methodCalls) {
                if (methodCall.getNameAsString().equals("add") || methodCall.getNameAsString().equals("remove")) {
                    Optional<Expression> optionalScope = methodCall.getScope();

                    if (optionalScope.isPresent()) {
                        Expression scope = optionalScope.get();

                        if (scope.isFieldAccessExpr()) {
                            FieldAccessExpr fieldAccessExpr = scope.asFieldAccessExpr();
                            String fieldAccessName = fieldAccessExpr.getNameAsString();
                            EligibleCollection operatesOn = null;

                            for (EligibleCollection eligibleCollection : eligibleCollections) {
                                if (eligibleCollection.getVariableDeclarator().getNameAsString().equals(fieldAccessName)) {
                                    operatesOn = eligibleCollection;
                                    break;
                                }
                            }

                            if (operatesOn == null) {
                                break;
                            }

                            // Does the method have a parameter which corresponds to the eligibleCollection,
                            // and is this parameter passed as argument?
                            // (ie. does the name one of the parameters match the argument of this function?)
                            NodeList<Expression> arguments = methodCall.getArguments();
                            Set<String> argumentSet = new HashSet<>();

                            for (Expression argument : arguments) {
                                if (argument.isNameExpr()) {
                                    argumentSet.add(((NameExpr) argument).getNameAsString());
                                }
                            }

                            for (Map.Entry<Parameter, String> parameterEntry : parametersOfInterest.entrySet()) {
                                Parameter parameter = parameterEntry.getKey();
                                String qualifiedName = parameterEntry.getValue();

                                if (qualifiedName.equals(operatesOn.getReferType().getQualifiedName())
                                        && argumentSet.contains(parameter.getNameAsString())) {
                                    if (methodCall.getNameAsString().equals("add")) {
                                        operatesOn.addAttachMethod(methodDeclaration);
                                    } else if (methodCall.getNameAsString().equals("remove")) {
                                        operatesOn.addDetachMethod(methodDeclaration);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Set<EligibleCollection> findEligibleCollections (final ClassOrInterfaceDeclaration classDeclaration) {
        Set<EligibleCollection> collections = new HashSet<>();

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
}
