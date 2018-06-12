package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates information on an abstract subject class.
 */
public class AbstractSubject extends SubjectClass {
    /**
     * A list of object properties containing references to abstract observers.
     */
    private List<ObserverCollection> observerCols;

    /**
     * AbstractSubject constructor.
     *
     * @param classDeclaration The class or interface defining the abstract subject
     */
    public AbstractSubject(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null, new ArrayList<>());
    }

    /**
     * AbstractSubject constructor.
     *
     * @param classDeclaration        The class or interface defining the abstract subject
     * @param resolvedType            The type of the abstract subject class
     */
    public AbstractSubject(final ClassOrInterfaceDeclaration classDeclaration,
                           final ResolvedReferenceTypeDeclaration resolvedType) {
        this(classDeclaration, resolvedType, new ArrayList<>());
    }

    /**
     *
     * @param classDeclaration        The class or interface defining the abstract subject
     * @param resolvedType            The type of the abstract subject class
     * @param observerCols            A list of object properties containing references to abstract observers.
     */
    public AbstractSubject(final ClassOrInterfaceDeclaration classDeclaration,
                           final ResolvedReferenceTypeDeclaration resolvedType,
                           final List<ObserverCollection> observerCols) {
        super(classDeclaration, resolvedType);
        this.observerCols = observerCols;
    }

    /**
     * @return A list of object properties containing references to abstract observers.
     */
    public List<ObserverCollection> getObserverCollections() {
        return observerCols;
    }

    /**
     * @param observerCol An object property containing references to abstract observers.
     */
    public void addObserverCollection(final ObserverCollection observerCol) {
        observerCols.add(observerCol);
    }
}
