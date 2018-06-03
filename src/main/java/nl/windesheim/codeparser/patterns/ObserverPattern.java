package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObservable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caveman on 4/19/18.
 */
public class ObserverPattern implements IDesignPattern {
    private ClassOrInterface abstractObservable;

    private ClassOrInterface abstractObserver;

    private List<ClassOrInterface> concreteObservables;

    private List<ClassOrInterface> concreteObservers;

    public ObserverPattern () {
        concreteObservables = new ArrayList<>();
        concreteObservers = new ArrayList<>();
    }

    public ClassOrInterface getAbstractObservable () {
        return abstractObservable;
    }

    public ObserverPattern setAbstractObservable (final ClassOrInterface abstractObservable) {
        this.abstractObservable = abstractObservable;
        return this;
    }

    public ClassOrInterface getAbstractObserver () {
        return abstractObserver;
    }

    public ObserverPattern setAbstractObserver (final ClassOrInterface abstractObserver) {
        this.abstractObserver = abstractObserver;
        return this;
    }

    public List<ClassOrInterface> getConcreteObservables () {
        return concreteObservables;
    }

    public ObserverPattern setConcreteObservables (final List<ClassOrInterface> concreteObservables) {
        this.concreteObservables = concreteObservables;
        return this;
    }

    public ObserverPattern addConcreteObservable (final ClassOrInterface concreteObservable) {
        this.concreteObservables.add(concreteObservable);
        return this;
    }

    public List<ClassOrInterface> getConcreteObservers () {
        return concreteObservers;
    }

    public ObserverPattern setConcreteObservers (final List<ClassOrInterface> concreteObservers) {
        this.concreteObservers = concreteObservers;
        return this;
    }

    public ObserverPattern addConcreteObserver (final ClassOrInterface concreteObserver) {
        this.concreteObservers.add(concreteObserver);
        return this;
    }
}
