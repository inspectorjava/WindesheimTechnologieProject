package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObservable;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.ObserverPattern;

import java.util.ArrayList;
import java.util.List;

public class ObserverAnalyzer extends PatternAnalyzer {

    /**
     * Finds relations between symbols.
     */
    private JavaSymbolSolver javaSymbolSolver;

    /**
     * A solver for data types.
     */
    private CombinedTypeSolver typeSolver;

    public ObserverAnalyzer() {
        super();
    }

    @Override
    public List<IDesignPattern> analyze(List<CompilationUnit> files) {
        // TODO Modify ImplementationOrSuperclassFinder to check for more ClassOrInterfaceDeclarations at once
        typeSolver = getParent().getTypeSolver();
        javaSymbolSolver = new JavaSymbolSolver(typeSolver);

        // Find 'abstract observable' classes
        AbstractObservableFinder abstractObservableFinder = new AbstractObservableFinder(typeSolver);

        for (CompilationUnit compilationUnit : files) {
            abstractObservableFinder.visit(compilationUnit, null);
        }

        List<ObserverPattern> observerPatterns = abstractObservableFinder.getObserverPatterns();

        // Search for classes that extend the AbstractObservable
        ConcreteObservableFinder concreteObservableFinder = new ConcreteObservableFinder(typeSolver, observerPatterns);
        for (CompilationUnit compilationUnit : files) {
            concreteObservableFinder.visit(compilationUnit, null);
        }

        AbstractObserverFinder abstractObserverFinder = new AbstractObserverFinder(typeSolver, observerPatterns);
        for (CompilationUnit compilationUnit : files) {
            abstractObserverFinder.visit(compilationUnit, null);
        }

        for (ObserverPattern observerPattern : observerPatterns) {
            System.out.println("Abstract Observable: " + observerPattern.getAbstractObservable().getResolvedTypeDeclaration().getQualifiedName());
            System.out.println("\tNumber of observer collections: " + observerPattern.getAbstractObservable().getObserverCollections().size());
            System.out.println("\tNumber of subclasses: " + observerPattern.getConcreteObservables().size());
            System.out.println("\tAbstract Observer: " + observerPattern.getAbstractObserver().getResolvedTypeDeclaration().getQualifiedName());
        }


        //  ConcreteObserver
            //  Bevat een referentie naar de Subject of een van zijn subclasses. Deze referentie mag in de superclass staan.
            //          Implementeert of overerft een update-methode: een methode die ofwel bij het subject de data ophaalt, ofwel deze informatie meegeleverd krijgt
            //  Het is mogelijk dat een klasse de in Java ingebouwde interface ObserverPattern implementeert, dit is een goede aanwijzing dat we te maken hebben met een ObserverPattern.

            // Bevat een referentie naar de Subject, of een van zn subclasses (nodig?)

        return new ArrayList<>();
    }
}
