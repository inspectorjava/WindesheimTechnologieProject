package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.CompilationUnit;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteSubject;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;

import java.util.List;

/**
 * Finds ConcreteSubjects: concrete implementations of an AbstractSubject.
 */
public class ConcreteSubjectFinder {
    /**
     * @param files            A list of files containing Java code
     * @param eligiblePatterns A list of potentially detected observer patterns, this will be updated
     *                         when concrete subjects have been found a pattern instance
     */
    public void findConcreteSubjects(
            final List<CompilationUnit> files,
            final List<EligibleObserverPattern> eligiblePatterns
    ) {
        ImplementationOrSuperclassFinder implFinder = new ImplementationOrSuperclassFinder();

        for (CompilationUnit compilationUnit : files) {
            for (EligibleObserverPattern observerPattern : eligiblePatterns) {
                implFinder.reset();
                implFinder.visit(compilationUnit, observerPattern.getAbstractSubject().getClassDeclaration());

                List<ConcreteSubject> concreteSubjects = ConcreteSubject.fromClasses(implFinder.getClasses());

                observerPattern.addConcreteSubject(concreteSubjects);
            }
        }
    }
}
