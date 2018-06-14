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
     * The file part which contains the abstract subject class or interface.
     */
    private ClassOrInterface abstractSubject;

    /**
     * The file part which contains the abstract observer class or interface.
     */
    private ClassOrInterface aObserver;

    /**
     * A list of file parts which contain concrete subject classes.
     */
    private List<ClassOrInterface> concreteSubjects;

    /**
     * A list of file parts which contain concrete observer classes.
     */
    private List<ClassOrInterface> cObservers;

    /**
     * Optional properties of this observer pattern.
     */
    private ObserverPatternProperties patternProps;

    /**
     * ObserverPattern constructor.
     */
    public ObserverPattern() {
        concreteSubjects = new ArrayList<>();
        cObservers = new ArrayList<>();
    }

    /**
     * @return The abstract subject in the observer pattern
     */
    public ClassOrInterface getAbstractSubject() {
        return abstractSubject;
    }

    /**
     * @param abstractSubject The abstract subject in the observer pattern
     * @return this
     */
    public ObserverPattern setAbstractSubject(final ClassOrInterface abstractSubject) {
        this.abstractSubject = abstractSubject;
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
     * @return A list of the concrete subjects in the observer pattern
     */
    public List<ClassOrInterface> getConcreteSubjects() {
        return concreteSubjects;
    }

    /**
     * @param concreteSubjects A list of the concrete subjects in the observer pattern
     * @return this
     */
    public ObserverPattern setConcreteSubjects(final List<ClassOrInterface> concreteSubjects) {
        this.concreteSubjects = concreteSubjects;
        return this;
    }

    /**
     * @param concreteSubject A concrete subject in the observer pattern
     * @return this
     */
    public ObserverPattern addConcreteSubject(final ClassOrInterface concreteSubject) {
        this.concreteSubjects.add(concreteSubject);
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

    /**
     * @return Optional properties of this observer pattern
     */
    public ObserverPatternProperties getPatternProperties() {
        return patternProps;
    }

    /**
     * @param patternProps Optional properties of this observer pattern
     * @return this
     */
    public ObserverPattern setPatternProperties(final ObserverPatternProperties patternProps) {
        this.patternProps = patternProps;
        return this;
    }
}
