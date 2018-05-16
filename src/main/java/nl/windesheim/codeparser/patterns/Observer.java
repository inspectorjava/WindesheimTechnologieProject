package nl.windesheim.codeparser.patterns;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents information about an identified Observer pattern.
 */
public class Observer implements IDesignPattern {
    /**
     * The probability that this is a correctly identified Observer pattern.
     */
    private boolean probability;

    /**
     * The class or interface that has been identified as Subject.
     */
    private ClassOrInterfaceDeclaration subject;

    /**
     * The classes that have been identified as ConcreteSubjects.
     */
    private List<ClassOrInterfaceDeclaration> concreteSubjects;

    /**
     * The class or interface that has been identified as Observer.
     */
    private ClassOrInterfaceDeclaration observer;

    /**
     * The classes that have been identified as ConcreteObservers.
     */
    private List<ClassOrInterfaceDeclaration> concreteObservers;

    /**
     * Observer constructor.
     */
    public Observer() {
        this.concreteSubjects = new ArrayList<>();
        this.concreteObservers = new ArrayList<>();
    }

    /**
     * @return The probability that this is a correctly identified Observer pattern
     */
    public boolean getProbability() {
        return this.probability;
    }

    /**
     *
     * @param probability Set the probability that this is a correctly identified Observer pattern
     * @return this
     */
    public Observer setProbability(final boolean probability) {
        this.probability = probability;
        return this;
    }

    /**
     * @return The class or interface that has been identified as Subject
     */
    public ClassOrInterfaceDeclaration getSubject() {
        return this.subject;
    }

    /**
     * @param subject A class or interface that has been identified as Subject
     * @return this
     */
    public Observer setSubject(final ClassOrInterfaceDeclaration subject) {
        this.subject = subject;
        return this;
    }

    /**
     * @return The classes that have been identified as ConcreteSubjects
     */
    public List<ClassOrInterfaceDeclaration> getConcreteSubjects() {
        return this.concreteSubjects;
    }

    /**
     * @param concreteSubject A class that has been identified as ConcreteSubject
     * @return this
     */
    public Observer addConcreteSubject(final ClassOrInterfaceDeclaration concreteSubject) {
        this.concreteSubjects.add(concreteSubject);
        return this;
    }

    /**
     * @param concreteSubject A class that has been identified as ConcreteSubject
     * @return this
     */
    public Observer removeConcreteSubject(final ClassOrInterfaceDeclaration concreteSubject) {
        this.concreteSubjects.remove(concreteSubject);
        return this;
    }

    /**
     * @return The class or interface that has been identified as Observer
     */
    public ClassOrInterfaceDeclaration getObserver() {
        return this.observer;
    }

    /**
     * @param observer A class or interface that has been identified as Observer
     * @return this
     */
    public Observer setObserver(final ClassOrInterfaceDeclaration observer) {
        this.observer = observer;
        return this;
    }

    /**
     * @return The classes that have been identified as ConcreteObservers
     */
    public List<ClassOrInterfaceDeclaration> getConcreteObservers() {
        return this.concreteObservers;
    }

    /**
     * @param concreteObserver A class that has been identified as ConcreteObserver
     * @return this
     */
    public Observer addConcreteObserver(final ClassOrInterfaceDeclaration concreteObserver) {
        this.concreteObservers.add(concreteObserver);
        return this;
    }

    /**
     * @param concreteObserver A class that has been identified as ConcreteObserver
     * @return this
     */
    public Observer removeConcreteObserver(final ClassOrInterfaceDeclaration concreteObserver) {
        this.concreteObservers.remove(concreteObserver);
        return this;
    }
}
