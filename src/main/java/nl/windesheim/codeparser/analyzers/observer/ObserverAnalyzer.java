package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.observer.componentfinders.ObserverPropertyFinder;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObservable;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObserver;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObservable;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObserver;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.ObserverPattern;
import nl.windesheim.codeparser.patterns.properties.ObserverPatternProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Finds occurrences of the observer pattern.
 *
 * An observer pattern is detected when
 * - The code contains a class which fits the criteria for being an AbstractObservable
 * - The code contains a class which fits the criteria for being an AbstractObserver
 * - (Optional) The code contains concrete implementations of observable classes
 * - (Optional) The code contains concrete implementations of observer classes
 */
public class ObserverAnalyzer extends PatternAnalyzer {
    @Override
    public List<IDesignPattern> analyze(final List<CompilationUnit> files) {
        List<IDesignPattern> patterns = new ArrayList<>();

        TypeSolver typeSolver = getTypeSolver();

        // Without a typesolver, the observeranalyzer cannot function
        if (typeSolver == null) {
            return patterns;
        }

        // Find abstract observable classes
        AbstractObservableFinder aObsableFinder = new AbstractObservableFinder(typeSolver);
        for (CompilationUnit compilationUnit : files) {
            aObsableFinder.visit(compilationUnit, null);
        }

        // Potential patterns, as found by analyzing code for abstract observables
        List<EligibleObserverPattern> rawPatterns = aObsableFinder.getObserverPatterns();
        if (rawPatterns.isEmpty()) {
            return patterns;
        }

        // Search for classes that extend the abstract observables
        ConcreteObservableFinder cObsableFinder = new ConcreteObservableFinder();
        cObsableFinder.findConcreteObservables(files, rawPatterns);

        // Find abstract observer classes
        AbstractObserverFinder aObserverFinder =
                new AbstractObserverFinder(typeSolver, rawPatterns);
        for (CompilationUnit compilationUnit : files) {
            aObserverFinder.visit(compilationUnit, null);
        }

        // Filter eligible patterns
        List<EligibleObserverPattern> eligiblePatterns = new ArrayList<>();
        for (EligibleObserverPattern rawPattern : rawPatterns) {
            if (rawPattern.getAbstractObserver() != null) {
                eligiblePatterns.add(rawPattern);
            }
        }

        // Search for classes that extend the abstract observers
        ConcreteObserverFinder cObserverFinder = new ConcreteObserverFinder();
        cObserverFinder.findConcreteObservers(files, eligiblePatterns);

        // Map eligible patterns to observer pattern results
        for (EligibleObserverPattern eligiblePattern : eligiblePatterns) {
            if (eligiblePattern.isObserverPattern()) {
                // Find properties
                ObserverPropertyFinder propertyFinder = new ObserverPropertyFinder(eligiblePattern);
                propertyFinder.findObserverProperties();

                patterns.add(makeObserverPattern(eligiblePattern));
            }
        }

        return patterns;
    }

    /**
     * Maps information on a potentially detected observer pattern to an object which encapsulates this information
     * in a format which is useful for the reporting tool.
     *
     * @param eligiblePattern A list of potential observer patterns which have already been detected
     * @return Object which encapsulates information on a detected observer pattern
     */
    private ObserverPattern makeObserverPattern(final EligibleObserverPattern eligiblePattern) {
        ObserverPattern observerPattern = new ObserverPattern();
        ObserverPatternProperties patternProps = new ObserverPatternProperties();

        // Fill abstract observable
        AbstractObservable aObservable = eligiblePattern.getAbstractObservable();
        observerPattern.setAbstractObservable(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(aObservable.getClassDeclaration()))
                        .setName(aObservable.getResolvedTypeDeclaration().getQualifiedName())
                        .setDeclaration(aObservable.getClassDeclaration())
        );

        patternProps.setObservableHasDetach(eligiblePattern.getActiveCollection().hasDetachMethods());


        // Fill abstract observer
        AbstractObserver aObserver = eligiblePattern.getAbstractObserver();
        observerPattern.setAbstractObserver(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(aObserver.getClassDeclaration()))
                        .setName(aObserver.getResolvedTypeDeclaration().getQualifiedName())
                        .setDeclaration(aObserver.getClassDeclaration())
        );

        patternProps
                .setObserverHasObservable(aObserver.getObservableVariable() != null)
                .setObserverHasAttachCall(aObserver.getHasAttachStatement())
                .setObserverHasDetachCall(aObserver.getHasDetachStatement())
                .setUpdateHasArguments(aObserver.isUpdateMethodHasArguments());


        // Fill concrete observable
        List<ConcreteObservable> cObservables = eligiblePattern.getConcreteObservables();
        for (ConcreteObservable cObservable : cObservables) {
            observerPattern.addConcreteObservable(
                    new ClassOrInterface()
                            .setFilePart(FilePartResolver.getFilePartOfNode(cObservable.getClassDeclaration()))
                            .setName(cObservable.getResolvedTypeDeclaration().getQualifiedName())
                            .setDeclaration(cObservable.getClassDeclaration())
            );
        }


        // Fill concrete observer
        List<ConcreteObserver> cObservers = eligiblePattern.getConcreteObservers();
        for (ConcreteObserver cObserver : cObservers) {
            observerPattern.addConcreteObserver(
                    new ClassOrInterface()
                            .setFilePart(FilePartResolver.getFilePartOfNode(cObserver.getClassDeclaration()))
                            .setName(cObserver.getResolvedTypeDeclaration().getQualifiedName())
                            .setDeclaration(cObserver.getClassDeclaration())
            );

            patternProps
                    .setObserverHasObservable(cObserver.getObservableVariable() != null)
                    .setObserverHasAttachCall(cObserver.getHasAttachStatement())
                    .setObserverHasDetachCall(cObserver.getHasDetachStatement())
                    .setUpdateHasArguments(aObserver.isUpdateMethodHasArguments());
        }

        observerPattern.setPatternProperties(patternProps);

        return observerPattern;
    }
}
