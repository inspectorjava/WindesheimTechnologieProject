package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.CompilationUnit;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObservable;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;

import java.util.List;

public class ConcreteObservableFinder {
    /**
     * Finds ConcreteObservables: concrete implementations of an AbstractObservable.
     *
     * @param files            A list of files containing Java code
     * @param eligiblePatterns A list of potentially detected observer patterns, this will be updated
     *                         when concrete observables have been found a pattern instance
     */
    public void findConcreteObservables(
            final List<CompilationUnit> files,
            final List<EligibleObserverPattern> eligiblePatterns
    ) {
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
}
