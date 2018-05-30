package nl.windesheim.codeparser.analyzers.observer.componentfinders;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.components.ObserverCollection;

import java.util.*;

public abstract class ObservableMethodFinder {
    protected TypeSolver typeSolver;
    protected List<ObserverCollection> observerCollections;

    public ObservableMethodFinder(TypeSolver typeSolver, List<ObserverCollection> observerCollections) {
        this.typeSolver = typeSolver;
        this.observerCollections = observerCollections;
    }

    public abstract void determine (final MethodDeclaration methodDeclaration);
}
