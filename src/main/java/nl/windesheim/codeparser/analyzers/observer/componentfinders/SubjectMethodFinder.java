package nl.windesheim.codeparser.analyzers.observer.componentfinders;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.components.ObserverCollection;

import java.util.List;

/**
 * Finds methods specific to an AbstractSubject.
 */
public abstract class SubjectMethodFinder {
    /**
     * A tool which resolved relations between AST nodes.
     */
    private final TypeSolver typeSolver;

    /**
     * A list of potential observer collections.
     */
    private final List<ObserverCollection> observerCols;

    /**
     * SubjectMethodFinder constructor.
     *
     * @param typeSolver   A TypeSolver which can be used by this class
     * @param observerCols A list of detected potential observer collections
     */
    public SubjectMethodFinder(
            final TypeSolver typeSolver,
            final List<ObserverCollection> observerCols
    ) {
        this.typeSolver = typeSolver;
        this.observerCols = observerCols;
    }

    /**
     * Determine whether the given method adheres to the criteria for an AbstractSubject method.
     *
     * @param methodDeclaration The method to analyze
     */
    public abstract void determine(MethodDeclaration methodDeclaration);

    /**
     * @return A tool which resolved relations between AST nodes
     */
    protected TypeSolver getTypeSolver() {
        return typeSolver;
    }

    /**
     * @return A list of potential observer collections
     */
    protected List<ObserverCollection> getObserverCollections() {
        return observerCols;
    }
}
