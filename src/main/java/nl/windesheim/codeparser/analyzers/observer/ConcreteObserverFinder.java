package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;

import java.util.List;

public class ConcreteObserverFinder extends VoidVisitorAdapter<Void> {
    private TypeSolver typeSolver;

    private List<EligibleObserverPattern> observerPatterns;

    public ConcreteObserverFinder(final TypeSolver typeSolver, final List<EligibleObserverPattern> observerPatterns) {
        super();
        this.typeSolver = typeSolver;
        this.observerPatterns = observerPatterns;
    }

    public void visit (ClassOrInterfaceDeclaration classDeclaration, Void arg) {

    }

}
