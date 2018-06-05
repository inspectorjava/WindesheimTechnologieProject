package nl.windesheim.codeparser.analyzers.observer.components;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates information on an abstract observable class.
 */
public class AbstractObservable extends ObservableClass {
    /**
     * A list of object properties containing references to abstract observers.
     */
    private List<ObserverCollection> observerCols;

    /**
     * AbstractObservable constructor.
     *
     * @param classDeclaration The class or interface defining the abstract observable
     */
    public AbstractObservable(final ClassOrInterfaceDeclaration classDeclaration) {
        this(classDeclaration, null, new ArrayList<>());
    }

    /**
     * AbstractObservable constructor.
     *
     * @param classDeclaration        The class or interface defining the abstract observable
     * @param resolvedType            The type of the abstract observable class
     */
    public AbstractObservable(final ClassOrInterfaceDeclaration classDeclaration,
                              final ResolvedReferenceTypeDeclaration resolvedType) {
        this(classDeclaration, resolvedType, new ArrayList<>());
    }

    /**
     *
     * @param classDeclaration        The class or interface defining the abstract observable
     * @param resolvedType            The type of the abstract observable class
     * @param observerCols            A list of object properties containing references to abstract observers.
     */
    public AbstractObservable(final ClassOrInterfaceDeclaration classDeclaration,
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
