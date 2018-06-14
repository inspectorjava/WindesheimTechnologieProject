package nl.windesheim.codeparser.analyzers.observer.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents information about an identified EligibleObserverPattern pattern.
 */
public class EligibleObserverPattern {
    /**
     * The class or interface that has been identified as abstract subject.
     */
    private AbstractSubject abstractSubject;

    /**
     * The classes that have been identified as concrete subjects.
     */
    private final List<ConcreteSubject> concreteSubjects;

    /**
     * The class or interface that has been identified as abstract observers.
     */
    private AbstractObserver abstractObserver;

    /**
     * The classes that have been identified as concrete observers.
     */
    private final List<ConcreteObserver> concreteObservers;

    /**
     * The observer collection which links the components in this pattern.
     */
    private ObserverCollection activeCollection;

    /**
     * EligibleObserverPattern constructor.
     */
    public EligibleObserverPattern() {
        this.abstractSubject = null;
        this.concreteSubjects = new ArrayList<>();

        this.abstractObserver = null;
        this.concreteObservers = new ArrayList<>();

        this.activeCollection = null;
    }

    /**
     * @return The class or interface that has been identified as abstract subject
     */
    public AbstractSubject getAbstractSubject() {
        return abstractSubject;
    }

    /**
     * @param abstractSubject The class or interface that has been identified as abstract subject
     * @return this
     */
    public EligibleObserverPattern setAbstractSubject(final AbstractSubject abstractSubject) {
        this.abstractSubject = abstractSubject;
        return this;
    }

    /**
     * @return The classes that have been identified as concrete subjects
     */
    public List<ConcreteSubject> getConcreteSubjects() {
        return concreteSubjects;
    }

    /**
     * @param concreteSubject A class that has been identified as concrete subject
     * @return this
     */
    public EligibleObserverPattern addConcreteSubject(final ConcreteSubject concreteSubject) {
        this.concreteSubjects.add(concreteSubject);
        return this;
    }

    /**
     * @param concreteSubjects A list of classes that have been identified as concrete subject
     * @return this
     */
    public EligibleObserverPattern addConcreteSubject(final List<ConcreteSubject> concreteSubjects) {
        this.concreteSubjects.addAll(concreteSubjects);
        return this;
    }

    /**
     * @return The class or interface that has been identified as abstract observers
     */
    public AbstractObserver getAbstractObserver() {
        return abstractObserver;
    }

    /**
     * @param abstractObserver The class or interface that has been identified as abstract observers
     * @return this
     */
    public EligibleObserverPattern setAbstractObserver(final AbstractObserver abstractObserver) {
        this.abstractObserver = abstractObserver;
        return this;
    }

    /**
     * @return Whether an abstract observer is found in the pattern
     */
    public boolean hasAbstractObserver() {
        return abstractObserver != null;
    }

    /**
     * @return The classes that have been identified as concrete observers
     */
    public List<ConcreteObserver> getConcreteObservers() {
        return concreteObservers;
    }

    /**
     * @param concreteObserver A class that has been identified as concrete observers
     * @return this
     */
    public EligibleObserverPattern addConcreteObserver(final ConcreteObserver concreteObserver) {
        this.concreteObservers.add(concreteObserver);
        return this;
    }

    /**
     * @param concreteObservers A list of classes that have been identified as concrete observers
     * @return this
     */
    public EligibleObserverPattern addConcreteObserver(final List<ConcreteObserver> concreteObservers) {
        this.concreteObservers.addAll(concreteObservers);
        return this;
    }

    /**
     * @return The observer collection which links the components in this pattern
     */
    public ObserverCollection getActiveCollection() {
        return activeCollection;
    }

    /**
     * @param activeCollection The observer collection which links the components in this pattern
     * @return this
     */
    public EligibleObserverPattern setActiveCollection(final ObserverCollection activeCollection) {
        this.activeCollection = activeCollection;
        return this;
    }

    /**
     * Determines whether the potential pattern is in fact a valid observer pattern.
     *
     * @return Whether this represents a valid observer pattern
     */
    public boolean isObserverPattern() {
        return abstractSubject != null && abstractObserver != null;
    }
}
