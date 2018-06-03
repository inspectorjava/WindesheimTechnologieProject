package nl.windesheim.codeparser.analyzers.observer.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents information about an identified EligibleObserverPattern pattern.
 */
public class EligibleObserverPattern {
    /**
     * The probability that this is a correctly identified EligibleObserverPattern pattern.
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
     * The class or interface that has been identified as EligibleObserverPattern.
     */
    private AbstractObserver abstractObserver;

    /**
     * The classes that have been identified as ConcreteObservers.
     */
    private List<ConcreteObserver> concreteObservers;

    /**
     * EligibleObserverPattern constructor.
     */
    public EligibleObserverPattern() {
        this.abstractObservable = null;
        this.concreteObservables = new ArrayList<>();

        this.abstractObserver = null;
        this.concreteObservers = new ArrayList<>();
    }

    /**
     * @return The probability that this is a correctly identified EligibleObserverPattern pattern
     */
    public boolean getProbability() {
        return this.probability;
    }

    /**
     *
     * @param probability Set the probability that this is a correctly identified EligibleObserverPattern pattern
     * @return this
     */
    public EligibleObserverPattern setProbability(final boolean probability) {
        this.probability = probability;
        return this;
    }

    public AbstractObservable getAbstractObservable() {
        return abstractObservable;
    }

    public EligibleObserverPattern setAbstractObservable (final AbstractObservable abstractObservable) {
        this.abstractObservable = abstractObservable;
        return this;
    }

    public List<ConcreteObservable> getConcreteObservables () {
        return concreteObservables;
    }

    public EligibleObserverPattern addConcreteObservable (final ConcreteObservable concreteObservable) {
        this.concreteObservables.add(concreteObservable);
        return this;
    }

    public EligibleObserverPattern addConcreteObservable (final List<ConcreteObservable> concreteObservables) {
        this.concreteObservables.addAll(concreteObservables);
        return this;
    }

    public AbstractObserver getAbstractObserver () {
        return abstractObserver;
    }

    public EligibleObserverPattern setAbstractObserver (final AbstractObserver abstractObserver) {
        this.abstractObserver = abstractObserver;
        return this;
    }

    public List<ConcreteObserver> getConcreteObservers () {
        return concreteObservers;
    }

    public EligibleObserverPattern addConcreteObserver (final ConcreteObserver concreteObserver) {
        this.concreteObservers.add(concreteObserver);
        return this;
    }

    public EligibleObserverPattern addConcreteObserver (final List<ConcreteObserver> concreteObservers) {
        this.concreteObservers.addAll(concreteObservers);
        return this;
    }
}
