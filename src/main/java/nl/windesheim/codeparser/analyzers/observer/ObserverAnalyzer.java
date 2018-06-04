package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObservable;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObserver;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObservable;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObserver;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.ObserverPattern;

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
        CombinedTypeSolver typeSolver = getParent().getTypeSolver();

        // Find abstract observable classes
        AbstractObservableFinder aObsableFinder = new AbstractObservableFinder(typeSolver);
        for (CompilationUnit compilationUnit : files) {
            aObsableFinder.visit(compilationUnit, null);
        }

        List<EligibleObserverPattern> eligiblePatterns = aObsableFinder.getObserverPatterns();

        // Search for classes that extend the abstract observables
        concreteObservableFinder(files, eligiblePatterns);

        // Find abstract observer classes
        AbstractObserverFinder aObserverFinder = new AbstractObserverFinder(typeSolver, eligiblePatterns);
        for (CompilationUnit compilationUnit : files) {
            aObserverFinder.visit(compilationUnit, null);
        }

        // Search for classes that extend the abstract observers
        findConcreteObservers(files, eligiblePatterns);

        List<IDesignPattern> patterns = new ArrayList<>();
        for (EligibleObserverPattern eligiblePattern : eligiblePatterns) {
            patterns.add(makeObserverPattern(eligiblePattern));
        }

        return patterns;
    }

    /**
     * Finds ConcreteObservables: concrete implementations of an AbstractObservable.
     *
     * @param files            A list of files containing Java code
     * @param eligiblePatterns A list of potentially detected observer patterns, this will be updated
     *                         when concrete observables have been found a pattern instance
     */
    private void concreteObservableFinder(final List<CompilationUnit> files,
                                          final List<EligibleObserverPattern> eligiblePatterns) {
        ImplementationOrSuperclassFinder implFinder = new ImplementationOrSuperclassFinder();

        for (CompilationUnit compilationUnit : files) {
            for (EligibleObserverPattern observerPattern : eligiblePatterns) {
                implFinder.reset();
                implFinder.visit(compilationUnit, observerPattern.getAbstractObservable().getClassDeclaration());

                List<ConcreteObservable> concObservables = ConcreteObservable.fromClasses(implFinder.getClasses());

                observerPattern.addConcreteObservable(concObservables);
            }
        }
    }

    /**
     * Finds ConcreteObservers: concrete implementations of an AbstractObserver.
     *
     * @param files            A list of files containing Java code
     * @param eligiblePatterns A list of potentially detected observer patterns, this will be updated
     *                         when concrete observers have been found for a pattern instance
     */
    private void findConcreteObservers(final List<CompilationUnit> files,
                                       final List<EligibleObserverPattern> eligiblePatterns
    ) {
        Map<EligibleObserverPattern, List<ClassOrInterfaceDeclaration>> observerMap =
                generateConcreteObserverMap(files, eligiblePatterns);

        filterConcreteObserverForUpdateMethod(observerMap);
    }

    /**
     * Generates map linking potential observer patterns to the found subclasses of it's abstract observer.
     *
     * @param files            A list of files containing Java code
     * @param eligiblePatterns The detected potential observer patterns
     * @return A map linking potential observer patterns to potential concrete observers
     */
    private Map<EligibleObserverPattern, List<ClassOrInterfaceDeclaration>> generateConcreteObserverMap(
            final List<CompilationUnit> files,
            final List<EligibleObserverPattern> eligiblePatterns
    ) {
        ImplementationOrSuperclassFinder implFinder = new ImplementationOrSuperclassFinder();
        Map<EligibleObserverPattern, List<ClassOrInterfaceDeclaration>> observerMap = new HashMap<>();

        for (CompilationUnit compilationUnit : files) {
            for (EligibleObserverPattern observerPattern : eligiblePatterns) {
                implFinder.reset();
                implFinder.visit(compilationUnit, observerPattern.getAbstractObserver().getClassDeclaration());

                // Check if the class actually implements the update method
                List<ClassOrInterfaceDeclaration> subclasses = implFinder.getClasses();

                if (!subclasses.isEmpty()) {
                    if (observerMap.containsKey(observerPattern)) {
                        observerMap.get(observerPattern).addAll(subclasses);
                    } else {
                        observerMap.put(
                                observerPattern,
                                new ArrayList<>(subclasses)
                        );
                    }
                }
            }
        }

        return observerMap;
    }

    /**
     * Checks whether the found subclasses of AbstractObserver contain a method with the same signature
     * as the update method in the abstract observer.
     *
     * @param observerMap A map linking potential observer patterns to the found subclasses
     *                    of it's abstract observer
     */
    private void filterConcreteObserverForUpdateMethod(
            final Map<EligibleObserverPattern, List<ClassOrInterfaceDeclaration>> observerMap
    ) {
        for (EligibleObserverPattern observerPattern : observerMap.keySet()) {
            List<ClassOrInterfaceDeclaration> subclasses = observerMap.get(observerPattern);

            //
            for (ClassOrInterfaceDeclaration subclass : subclasses) {
                List<MethodDeclaration> subclassMethods =  subclass.getMethods();
                boolean extendsUpdate = false;

                for (MethodDeclaration method : subclassMethods) {
                    ResolvedMethodDeclaration abstractUpdate =
                            observerPattern.getAbstractObserver().getUpdateMethod();
                    if (!(abstractUpdate instanceof JavaParserMethodDeclaration)) {
                        continue;
                    }

                    MethodDeclaration targetMethod =
                            ((JavaParserMethodDeclaration) abstractUpdate).getWrappedNode();
                    CallableDeclaration.Signature targetSignature = targetMethod.getSignature();
                    if (method.getSignature().equals(targetSignature)) {
                        extendsUpdate = true;
                        break;
                    }
                }

                if (extendsUpdate) {
                    observerPattern.addConcreteObserver(new ConcreteObserver(subclass));
                }
            }
        }
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

        // Fill abstract observable
        AbstractObservable aObservable = eligiblePattern.getAbstractObservable();
        observerPattern.setAbstractObservable(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(aObservable.getClassDeclaration()))
                        .setName(aObservable.getResolvedTypeDeclaration().getQualifiedName())
                        .setDeclaration(aObservable.getClassDeclaration())
        );

        // Fill abstract observer
        AbstractObserver aObserver = eligiblePattern.getAbstractObserver();
        observerPattern.setAbstractObserver(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(aObserver.getClassDeclaration()))
                        .setName(aObserver.getResolvedTypeDeclaration().getQualifiedName())
                        .setDeclaration(aObserver.getClassDeclaration())
        );

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
        }

        return observerPattern;
    }
}
