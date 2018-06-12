package nl.windesheim.codeparser.analyzers.observer.componentfinders;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import nl.windesheim.codeparser.analyzers.observer.components.*;

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
        // TODO Probably needs some refactoring
        ObserverCollection collection = pattern.getActiveCollection();
        if (collection == null) {
            return;
        }

        VariableDeclarator observableVar = null;
        if (observer instanceof ConcreteObserver && pattern.getAbstractObserver().getObservableVariable() != null) {
            observableVar = pattern.getAbstractObserver().getObservableVariable();
        } else {
            // Check whether the observer refers to an observable in a class member
            ObservableVariableFinder obsVarFinder = new ObservableVariableFinder();

            List<ObservableClass> observables = new ArrayList<>();
            observables.add(pattern.getAbstractObservable());
            if (observer instanceof ConcreteObserver) {
                observables.addAll(pattern.getConcreteObservables());
            }

            observableVar = obsVarFinder.findObservableVariable(observer, observables);
        }

        if (observableVar != null) {
            findMethodCalls(observer, observableVar, collection);
        }
    }

    private void findMethodCalls (final ObserverClass observer, final VariableDeclarator observableVar, final ObserverCollection collection) {
        observer.setObservableVariable(observableVar);

        // Check whether there's a call to the attach method
        FieldMethodCallFinder methodCallFinder = new FieldMethodCallFinder();

        Boolean hasAttachCalls =
                methodCallFinder.visit(observer.getClassDeclaration(), collection.getAttachMethods());
        if (hasAttachCalls != null && hasAttachCalls) {
            observer.setHasAttachStatement(true);
        }

        // Check whether there's a call to the detach method
        Boolean hasDetachCalls =
                methodCallFinder.visit(observer.getClassDeclaration(), collection.getDetachMethods());
        if (hasDetachCalls != null && hasDetachCalls) {
            observer.setHasDetachStatement(true);
        }
    }

    private boolean hasAttachCalls (ClassOrInterfaceDeclaration classDecl, List<MethodDeclaration> methodDecls) {
        List<MethodCallExpr> methodCalls = classDecl.findAll(MethodCallExpr.class);
        List<ResolvedMethodDeclaration> resMethodDecls = new ArrayList<>();
        for (MethodDeclaration methodDecl : methodDecls) {
            resMethodDecls.add(methodDecl.resolve());
        }

        for (MethodCallExpr methodCallExpr : methodCalls) {
            ResolvedMethodDeclaration resolvedCall = methodCallExpr.resolve();
            for (ResolvedMethodDeclaration resMethodDecl : resMethodDecls) {
                if (resMethodDecl.getQualifiedSignature().equals(resolvedCall.getQualifiedSignature())) {
                    return true;
                }
            }
        }

        return false;
    }
}
