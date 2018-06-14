package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.observer.componentfinders.ObserverPropertyFinder;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObserver;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractSubject;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObserver;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteSubject;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.ObserverPattern;
import nl.windesheim.codeparser.patterns.properties.ObserverPatternProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds occurrences of the observer pattern.
 *
 * An observer pattern is detected when
 * - The code contains a class which fits the criteria for being an AbstractSubject
 * - The code contains a class which fits the criteria for being an AbstractObserver
 * - (Optional) The code contains concrete implementations of subject classes
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

        // Find abstract subject classes
        AbstractSubjectFinder aSubjectFinder = new AbstractSubjectFinder(typeSolver);
        for (CompilationUnit compilationUnit : files) {
            aSubjectFinder.visit(compilationUnit, null);
        }

        // Potential patterns, as found by analyzing code for abstract subjects
        List<EligibleObserverPattern> rawPatterns = aSubjectFinder.getObserverPatterns();
        if (rawPatterns.isEmpty()) {
            return patterns;
        }

        // Search for classes that extend the abstract subjects
        ConcreteSubjectFinder cSubjectFinder = new ConcreteSubjectFinder();
        cSubjectFinder.findConcreteSubjects(files, rawPatterns);

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

        // Fill abstract subject
        AbstractSubject abstractSubject = eligiblePattern.getAbstractSubject();
        observerPattern.setAbstractSubject(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(abstractSubject.getClassDeclaration()))
                        .setName(abstractSubject.getResolvedTypeDeclaration().getQualifiedName())
                        .setDeclaration(abstractSubject.getClassDeclaration())
        );

        patternProps.setSubjectHasDetach(eligiblePattern.getActiveCollection().hasDetachMethods());


        // Fill abstract observer
        AbstractObserver aObserver = eligiblePattern.getAbstractObserver();
        observerPattern.setAbstractObserver(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(aObserver.getClassDeclaration()))
                        .setName(aObserver.getResolvedTypeDeclaration().getQualifiedName())
                        .setDeclaration(aObserver.getClassDeclaration())
        );

        patternProps
                .setObserverHasSubject(aObserver.getSubjectVariable() != null)
                .setObserverHasAttachCall(aObserver.isHasAttachStatement())
                .setObserverHasDetachCall(aObserver.isHasDetachStatement())
                .setUpdateHasArguments(aObserver.isUpdateMethodHasParameters());


        // Fill concrete subject
        List<ConcreteSubject> concreteSubjects = eligiblePattern.getConcreteSubjects();
        for (ConcreteSubject concreteSubject : concreteSubjects) {
            observerPattern.addConcreteSubject(
                    new ClassOrInterface()
                            .setFilePart(FilePartResolver.getFilePartOfNode(concreteSubject.getClassDeclaration()))
                            .setName(concreteSubject.getResolvedTypeDeclaration().getQualifiedName())
                            .setDeclaration(concreteSubject.getClassDeclaration())
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
                    .setObserverHasSubject(cObserver.getSubjectVariable() != null)
                    .setObserverHasAttachCall(cObserver.isHasAttachStatement())
                    .setObserverHasDetachCall(cObserver.isHasDetachStatement())
                    .setUpdateHasArguments(aObserver.isUpdateMethodHasParameters());
        }

        observerPattern.setPatternProperties(patternProps);

        return observerPattern;
    }
}
