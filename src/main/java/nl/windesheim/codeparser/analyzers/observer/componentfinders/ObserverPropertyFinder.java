package nl.windesheim.codeparser.analyzers.observer.componentfinders;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObserver;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObserver;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;
import nl.windesheim.codeparser.analyzers.observer.components.ObserverClass;
import nl.windesheim.codeparser.analyzers.observer.components.ObserverCollection;
import nl.windesheim.codeparser.analyzers.observer.components.SubjectClass;
import nl.windesheim.codeparser.analyzers.util.visitor.MethodCallFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Searches for certain properties specific to observer classes.
 *
 * - Whether the class contains a reference to an instance of a Subject class
 * - Whether there's a call to the attach method
 * - Whether there's a call to the detach method
 */
public class ObserverPropertyFinder {
    /**
     * A potential observer pattern to analyze.
     */
    private EligibleObserverPattern pattern;

    /**
     * ObserverPropertyFinder constructor.
     *
     * @param pattern A potential observer pattern to analyze
     */
    public ObserverPropertyFinder(final EligibleObserverPattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Finds observer properties in the observer classes in the pattern.
     */
    public void findObserverProperties() {
        // Check whether the abstract observer has properties
        AbstractObserver abstractObserver = pattern.getAbstractObserver();

        if (!abstractObserver.getClassDeclaration().isInterface()) {
            findProperties(abstractObserver);
        }

        // Check whether the concrete observers have properties
        for (ConcreteObserver concreteObserver : pattern.getConcreteObservers()) {
            findProperties(concreteObserver);
        }
    }

    /**
     * Finds observer-specific properties in an observer class.
     *
     * @param observer The observer class to find the properties in
     */
    private void findProperties(final ObserverClass observer) {
        ObserverCollection collection = pattern.getActiveCollection();
        if (collection == null) {
            return;
        }

        // Find reference to subject
        VariableDeclarator subjectVar;
        if (observer instanceof ConcreteObserver && pattern.getAbstractObserver().getSubjectVariable() != null) {
            subjectVar = pattern.getAbstractObserver().getSubjectVariable();
        } else {
            // Check whether the observer refers to an subject in a class member
            SubjectVariableFinder obsVarFinder = new SubjectVariableFinder();

            List<SubjectClass> subjects = new ArrayList<>();
            subjects.add(pattern.getAbstractSubject());
            if (observer instanceof ConcreteObserver) {
                subjects.addAll(pattern.getConcreteSubjects());
            }

            subjectVar = obsVarFinder.findSubjectVariable(observer, subjects);
        }

        if (subjectVar != null) {
            observer.setSubjectVariable(subjectVar);
        }

        // Find calls to the attach and detach methods
        findMethodCalls(observer, collection);
    }

    /**
     * Finds calls to attach and detach method.
     *
     * @param observer   The observer class to find the properties in
     * @param collection The active observer collection which contains references to the attach and detach methods
     */
    private void findMethodCalls(final ObserverClass observer, final ObserverCollection collection) {
        // Check whether there's a call to the attach method
        MethodCallFinder methodCallFinder = new MethodCallFinder();

        List<ResolvedMethodDeclaration> resAttachMethods = resolveMethodDeclarations(collection.getAttachMethods());

        Boolean hasAttachCalls =
                methodCallFinder.visit(observer.getClassDeclaration(), resAttachMethods);
        if (hasAttachCalls != null && hasAttachCalls) {
            observer.setHasAttachStatement(true);
        }

        // Check whether there's a call to the detach method
        if (collection.hasDetachMethods()) {
            List<ResolvedMethodDeclaration> resDetachMethods =
                    resolveMethodDeclarations(collection.getDetachMethods());

            Boolean hasDetachCalls =
                    methodCallFinder.visit(observer.getClassDeclaration(), resDetachMethods);
            if (hasDetachCalls != null && hasDetachCalls) {
                observer.setHasDetachStatement(true);
            }
        }
    }

    /**
     * Resolves a list of method declarations.
     *
     * @param methodDecls The method declarations to resolve
     * @return A list of resolved method declarations
     */
    private List<ResolvedMethodDeclaration> resolveMethodDeclarations(final List<MethodDeclaration> methodDecls) {
        List<ResolvedMethodDeclaration> resMethodDecls = new ArrayList<>();

        for (MethodDeclaration method : methodDecls) {
            resMethodDecls.add(method.resolve());
        }

        return resMethodDecls;
    }

}
