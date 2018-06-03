package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates information on a detected Observer pattern.
 */
public class ObserverPattern implements IDesignPattern {
    /**
     * The file part which contains the abstract observable class or interface.
     */
    private ClassOrInterface aObservable;

    /**
     * The file part which contains the abstract observer class or interface.
     */
    private ClassOrInterface aObserver;

    /**
     * A list of file parts which contain concrete observable classes.
     */
    private List<ClassOrInterface> cObservables;

    /**
     * A list of file parts which contain concrete observer classes.
     */
    private List<ClassOrInterface> cObservers;

    /**
     * ObserverPattern constructor.
     */
    public ObserverPattern() {
        cObservables = new ArrayList<>();
        cObservers = new ArrayList<>();
    }

    /**
     * @return The abstract observable in the observer pattern
     */
    public ClassOrInterface getAbstractObservable() {
        return aObservable;
    }

    /**
     * @param abstractObservable The abstract observable in the observer pattern
     * @return this
     */
    public ObserverPattern setAbstractObservable(final ClassOrInterface abstractObservable) {
        this.aObservable = abstractObservable;
        return this;
    }

    /**
     * @return The abstract observer in the observer pattern
     */
    public ClassOrInterface getAbstractObserver() {
        return aObserver;
    }

    /**
     * @param abstractObserver The abstract observer in the observer pattern
     * @return this
     */
    public ObserverPattern setAbstractObserver(final ClassOrInterface abstractObserver) {
        this.aObserver = abstractObserver;
        return this;
    }

    /**
     * @return A list of the concrete observables in the observer pattern
     */
    public List<ClassOrInterface> getConcreteObservables() {
        return cObservables;
    }

    /**
     * @param concreteObservables A list of the concrete observables in the observer pattern
     * @return this
     */
    public ObserverPattern setConcreteObservables(final List<ClassOrInterface> concreteObservables) {
        this.cObservables = concreteObservables;
        return this;
    }

    /**
     * @param concreteObservable A concrete observable in the observer pattern
     * @return this
     */
    public ObserverPattern addConcreteObservable(final ClassOrInterface concreteObservable) {
        this.cObservables.add(concreteObservable);
        return this;
    }

    /**
     * @return A list of the concrete observers in the observer pattern
     */
    public List<ClassOrInterface> getConcreteObservers() {
        return cObservers;
    }

    /**
     * @param concreteObservers A list of the concrete observers in the observer pattern
     * @return this
     */
    public ObserverPattern setConcreteObservers(final List<ClassOrInterface> concreteObservers) {
        this.cObservers = concreteObservers;
        return this;
    }

    /**
     * @param concreteObserver A concrete observer in the observer pattern
     * @return this
     */
    public ObserverPattern addConcreteObserver(final ClassOrInterface concreteObserver) {
        this.cObservers.add(concreteObserver);
        return this;
    }
}
