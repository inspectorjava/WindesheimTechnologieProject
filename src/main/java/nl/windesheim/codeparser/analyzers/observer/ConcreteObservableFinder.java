package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObservable;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;

import java.util.List;

public class ConcreteObservableFinder
        extends VoidVisitorAdapter<Void> {

    private TypeSolver typeSolver;

    private List<EligibleObserverPattern> observerPatterns;

    public ConcreteObservableFinder (final TypeSolver typeSolver, final List<EligibleObserverPattern> observerPatterns) {
        super();
        this.typeSolver = typeSolver;
        this.observerPatterns = observerPatterns;
    }

    public void visit (final ClassOrInterfaceDeclaration classDeclaration, Void arg) {
        // TODO Speciaal geval fixen voor een subklasse van Observable fixen

        // Check of deze klasse overerft van een van de abstractobservables
        if (!classDeclaration.isInterface()) {
            for (ClassOrInterfaceType superType : classDeclaration.getExtendedTypes()) {
                try {
                    ResolvedReferenceTypeDeclaration resolvedSuperTypeDeclaration = superType.resolve().getTypeDeclaration();

                    // Check of een van de supertypes overeenkomt met een van de gevonden AbstractObservables
                    for (EligibleObserverPattern observerPattern : observerPatterns) {
                        if (observerPattern.getAbstractObservable().getResolvedTypeDeclaration().equals(resolvedSuperTypeDeclaration)) {
                            ConcreteObservable concreteObservable = new ConcreteObservable(classDeclaration, classDeclaration.resolve());
                            observerPattern.addConcreteObservable(concreteObservable);
                            break;
                        }
                    }
                } catch (UnsolvedSymbolException ex) {
                    // FIXME Fix exception log
                }

            }
        }
    }
}
