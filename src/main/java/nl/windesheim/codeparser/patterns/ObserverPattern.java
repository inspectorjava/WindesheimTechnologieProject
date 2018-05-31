package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.analyzers.observer.components.AbstractObservable;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObserver;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObservable;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents information about an identified ObserverPattern pattern.
 */
public class ObserverPattern implements IDesignPattern {
    /**
     * The probability that this is a correctly identified ObserverPattern pattern.
     */
    private boolean probability;

    /**
     * The class or interface that has been identified as Subject.
     */
    private AbstractObservable abstractObservable;

    /**
     * The classes that have been identified as ConcreteSubjects.
     */
    private List<ConcreteObservable> concreteObservables;

    /**
     * The class or interface that has been identified as ObserverPattern.
     */
    private AbstractObserver abstractObserver;

    /**
     * The classes that have been identified as ConcreteObservers.
     */
    private List<ConcreteObserver> concreteObservers;

    /**
     * ObserverPattern constructor.
     */
    public ObserverPattern() {
        this.abstractObservable = null;
        this.concreteObservables = new ArrayList<>();

        this.abstractObserver = null;
        this.concreteObservers = new ArrayList<>();
    }

    /**
     * @return The probability that this is a correctly identified ObserverPattern pattern
     */
    public boolean getProbability() {
        return this.probability;
    }

    /**
     *
     * @param probability Set the probability that this is a correctly identified ObserverPattern pattern
     * @return this
     */
    public ObserverPattern setProbability(final boolean probability) {
        this.probability = probability;
        return this;
    }

    public AbstractObservable getAbstractObservable() {
        return abstractObservable;
    }

    public ObserverPattern setAbstractObservable (final AbstractObservable abstractObservable) {
        this.abstractObservable = abstractObservable;
        return this;
    }

    public List<ConcreteObservable> getConcreteObservables () {
        return concreteObservables;
    }

    public ObserverPattern setConcreteObservables (final List<ConcreteObservable> concreteObservables) {
        this.concreteObservables = concreteObservables;
        return this;
    }

    public ObserverPattern addConcreteObservable (final ConcreteObservable concreteObservable) {
        this.concreteObservables.add(concreteObservable);
        return this;
    }

    public AbstractObserver getAbstractObserver () {
        return abstractObserver;
    }

    public ObserverPattern setAbstractObserver (final AbstractObserver abstractObserver) {
        this.abstractObserver = abstractObserver;
        return this;
    }
}
