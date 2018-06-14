package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObserver;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Finds ConcreteObservers: concrete implementations of an AbstractObserver.
 */
public class ConcreteObserverFinder {
    /**
     * @param files            A list of files containing Java code
     * @param eligiblePatterns The detected potential observer patterns
     */
    public void findConcreteObservers(
            final List<CompilationUnit> files,
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
}
