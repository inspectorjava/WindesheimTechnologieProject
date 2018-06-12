package nl.windesheim.codeparser.analyzers.observer.componentfinders;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import nl.windesheim.codeparser.analyzers.observer.components.*;
import nl.windesheim.codeparser.analyzers.util.visitor.MethodCallFinder;

import java.util.ArrayList;
import java.util.List;

public class ObserverPropertyFinder {
    private EligibleObserverPattern pattern;

    public ObserverPropertyFinder (EligibleObserverPattern pattern) {
        this.pattern = pattern;
    }

    public void findObserverProperties () {
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

    private void findProperties (final ObserverClass observer) {
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

    private void findMethodCalls (final ObserverClass observer, final ObserverCollection collection) {
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
            List<ResolvedMethodDeclaration> resDetachMethods = resolveMethodDeclarations(collection.getDetachMethods());

            Boolean hasDetachCalls =
                    methodCallFinder.visit(observer.getClassDeclaration(), resDetachMethods);
            if (hasDetachCalls != null && hasDetachCalls) {
                observer.setHasDetachStatement(true);
            }
        }
    }

    private List<ResolvedMethodDeclaration> resolveMethodDeclarations (List<MethodDeclaration> methodDecls) {
        List<ResolvedMethodDeclaration> resMethodDecls = new ArrayList<>();

        for (MethodDeclaration method : methodDecls) {
            resMethodDecls.add(method.resolve());
        }

        return resMethodDecls;
    }

}
