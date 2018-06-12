package nl.windesheim.codeparser.analyzers.observer.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents information about an identified EligibleObserverPattern pattern.
 */
public class EligibleObserverPattern {
    /**
     * The class or interface that has been identified as abstract observable.
     */
    private AbstractObservable aObservable;

    /**
     * The classes that have been identified as concrete observables.
     */
    private final List<ConcreteObservable> cObservables;

    /**
     * The class or interface that has been identified as abstract observers.
     */
    private AbstractObserver aObserver;

    /**
     * The classes that have been identified as concrete observers.
     */
    private final List<ConcreteObserver> cObservers;

    private ObserverCollection activeCollection;

    /**
     * EligibleObserverPattern constructor.
     */
    public EligibleObserverPattern() {
        this.aObservable = null;
        this.cObservables = new ArrayList<>();

        this.aObserver = null;
        this.cObservers = new ArrayList<>();

        this.activeCollection = null;
    }

    /**
     * @return The class or interface that has been identified as abstract observable
     */
    public AbstractObservable getAbstractObservable() {
        return aObservable;
    }

    /**
     * @param aObservable The class or interface that has been identified as abstract observable
     * @return this
     */
    public EligibleObserverPattern setAbstractObservable(final AbstractObservable aObservable) {
        this.aObservable = aObservable;
        return this;
    }

    /**
     * @return The classes that have been identified as concrete observables
     */
    public List<ConcreteObservable> getConcreteObservables() {
        return cObservables;
    }

    /**
     * @param cObservable A class that has been identified as concrete observable
     * @return this
     */
    public EligibleObserverPattern addConcreteObservable(final ConcreteObservable cObservable) {
        this.cObservables.add(cObservable);
        return this;
    }

    /**
     * @param cObservables A list of classes that have been identified as concrete observable
     * @return this
     */
    public EligibleObserverPattern addConcreteObservable(final List<ConcreteObservable> cObservables) {
        this.cObservables.addAll(cObservables);
        return this;
    }

    /**
     * @return The class or interface that has been identified as abstract observers
     */
    public AbstractObserver getAbstractObserver() {
        return aObserver;
    }

    /**
     * @param aObserver The class or interface that has been identified as abstract observers
     * @return this
     */
    public EligibleObserverPattern setAbstractObserver(final AbstractObserver aObserver) {
        this.aObserver = aObserver;
        return this;
    }

    public boolean hasAbstractObserver () {
        return aObserver != null;
    }

    /**
     * @return The classes that have been identified as concrete observers
     */
    public List<ConcreteObserver> getConcreteObservers() {
        return cObservers;
    }

    /**
     * @param cObserver A class that has been identified as concrete observers
     * @return this
     */
    public EligibleObserverPattern addConcreteObserver(final ConcreteObserver cObserver) {
        this.cObservers.add(cObserver);
        return this;
    }

    /**
     * @param cObservers A list of classes that have been identified as concrete observers
     * @return this
     */
    public EligibleObserverPattern addConcreteObserver(final List<ConcreteObserver> cObservers) {
        this.cObservers.addAll(cObservers);
        return this;
    }

    public ObserverCollection getActiveCollection () {
        return activeCollection;
    }

    public EligibleObserverPattern setActiveCollection (final ObserverCollection activeCollection) {
        this.activeCollection = activeCollection;
        return this;
    }

    /**
     * Determines whether the potential pattern is in fact a valid observer pattern.
     *
     * @return Whether this represents a valid observer pattern
     */
    public boolean isObserverPattern() {
        return aObservable != null && aObserver != null;
    }
}
