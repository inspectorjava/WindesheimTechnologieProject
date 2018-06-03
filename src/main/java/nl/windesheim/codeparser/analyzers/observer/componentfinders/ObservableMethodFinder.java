package nl.windesheim.codeparser.analyzers.observer.componentfinders;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.components.ObserverCollection;

import java.util.List;

/**
 * Finds methods specific to an AbstractObservable.
 */
public abstract class ObservableMethodFinder {
    /**
     * A tool which resolved relations between AST nodes.
     */
    private final TypeSolver typeSolver;

    /**
     * A list of potential observer collections.
     */
    private final List<ObserverCollection> observerCols;

    /**
     * ObservableMethodFinder constructor.
     *
     * @param typeSolver          A TypeSolver which can be used by this class
     * @param observerCols A list of detected potential observer collections
     */
    public ObservableMethodFinder(final TypeSolver typeSolver, final List<ObserverCollection> observerCols) {
        this.typeSolver = typeSolver;
        this.observerCols = observerCols;
    }

    /**
     * Determine whether the given method adheres to the criteria for an AbstractObservable method.
     *
     * @param methodDeclaration The method to analyze
     */
    public abstract void determine(final MethodDeclaration methodDeclaration);

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
