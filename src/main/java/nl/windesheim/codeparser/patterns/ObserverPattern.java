package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.patterns.properties.ObserverPatternProperties;

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

    private ObserverPatternProperties patternProps;

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
     * @param aObservable The abstract observable in the observer pattern
     * @return this
     */
    public ObserverPattern setAbstractObservable(final ClassOrInterface aObservable) {
        this.aObservable = aObservable;
        return this;
    }

    /**
     * @return The abstract observer in the observer pattern
     */
    public ClassOrInterface getAbstractObserver() {
        return aObserver;
    }

    /**
     * @param aObserver The abstract observer in the observer pattern
     * @return this
     */
    public ObserverPattern setAbstractObserver(final ClassOrInterface aObserver) {
        this.aObserver = aObserver;
        return this;
    }

    /**
     * @return A list of the concrete observables in the observer pattern
     */
    public List<ClassOrInterface> getConcreteObservables() {
        return cObservables;
    }

    /**
     * @param cObservers A list of the concrete observables in the observer pattern
     * @return this
     */
    public ObserverPattern setConcreteObservables(final List<ClassOrInterface> cObservers) {
        this.cObservables = cObservers;
        return this;
    }

    /**
     * @param cObservable A concrete observable in the observer pattern
     * @return this
     */
    public ObserverPattern addConcreteObservable(final ClassOrInterface cObservable) {
        this.cObservables.add(cObservable);
        return this;
    }

    /**
     * @return A list of the concrete observers in the observer pattern
     */
    public List<ClassOrInterface> getConcreteObservers() {
        return cObservers;
    }

    /**
     * @param cObservers A list of the concrete observers in the observer pattern
     * @return this
     */
    public ObserverPattern setConcreteObservers(final List<ClassOrInterface> cObservers) {
        this.cObservers = cObservers;
        return this;
    }

    /**
     * @param cObserver A concrete observer in the observer pattern
     * @return this
     */
    public ObserverPattern addConcreteObserver(final ClassOrInterface cObserver) {
        this.cObservers.add(cObserver);
        return this;
    }

    public ObserverPatternProperties getPatternProperties() {
        return patternProps;
    }

    public ObserverPattern setPatternProperties (final ObserverPatternProperties patternProps) {
        this.patternProps = patternProps;
        return this;
    }
}
