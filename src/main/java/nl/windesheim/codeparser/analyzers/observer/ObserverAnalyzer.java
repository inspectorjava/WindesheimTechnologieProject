package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObservable;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObservable;
import nl.windesheim.codeparser.analyzers.observer.components.ConcreteObserver;
import nl.windesheim.codeparser.analyzers.observer.components.EligibleObserverPattern;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
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

        List<EligibleObserverPattern> eligiblePatterns = abstractObservableFinder.getObserverPatterns();

        // Search for classes that extend the AbstractObservable
        concreteObservableFinder(files, eligiblePatterns);

//        ConcreteObservableFinder concreteObservableFinder = new ConcreteObservableFinder(typeSolver, eligiblePatterns);
//        for (CompilationUnit compilationUnit : files) {
//            concreteObservableFinder.visit(compilationUnit, null);
//        }

        AbstractObserverFinder abstractObserverFinder = new AbstractObserverFinder(typeSolver, eligiblePatterns);
        for (CompilationUnit compilationUnit : files) {
            abstractObserverFinder.visit(compilationUnit, null);
        }

        //  ConcreteObserver
        concreteObserverFinder(files, eligiblePatterns);

        for (EligibleObserverPattern observerPattern : eligiblePatterns) {
            System.out.println("--- Observer pattern ---");
            System.out.println("\tAbstract Observable: " + observerPattern.getAbstractObservable().getResolvedTypeDeclaration().getQualifiedName());

            System.out.println("\tConcrete observables: " + observerPattern.getConcreteObservables().size());
            for (ConcreteObservable concreteObservable : observerPattern.getConcreteObservables()) {
                System.out.println("\t\t- " + concreteObservable.getResolvedTypeDeclaration().getQualifiedName());
            }

            System.out.println("\tAbstract Observer: " + observerPattern.getAbstractObserver().getResolvedTypeDeclaration().getQualifiedName());

            System.out.println("\tConcrete observers: " + observerPattern.getConcreteObservers().size());
            for (ConcreteObserver concreteObserver : observerPattern.getConcreteObservers()) {
                System.out.println("\t\t- " + concreteObserver.getResolvedTypeDeclaration().getQualifiedName());
            }
        }

        List<IDesignPattern> patterns = new ArrayList<>();
        for (EligibleObserverPattern eligiblePattern : eligiblePatterns) {
            // Check of het patroon aan bepaalde voorwaarden voldoet?
            patterns.add(makeObserverPattern(eligiblePattern));
        }

        return patterns;
    }

    // TODO Dit schijnt mooi met een command pattern te kunnen
    private void concreteObservableFinder (List<CompilationUnit> files, List<EligibleObserverPattern> eligiblePatterns) {
        ImplementationOrSuperclassFinder implFinder = new ImplementationOrSuperclassFinder();

        for (CompilationUnit compilationUnit : files) {
            for (EligibleObserverPattern observerPattern : eligiblePatterns) {
                implFinder.reset();
                implFinder.visit(compilationUnit, observerPattern.getAbstractObservable().getClassDeclaration());

                List<ConcreteObservable> concreteObservables = ConcreteObservable.fromClasses(implFinder.getClasses());

                observerPattern.addConcreteObservable(concreteObservables);
            }
        }
    }

    // TODO Dit schijnt mooi met een command pattern te kunnen
    private void concreteObserverFinder (List<CompilationUnit> files, List<EligibleObserverPattern> eligiblePatterns) {
        //  Bevat een referentie naar de Subject of een van zijn subclasses. Deze referentie mag in de superclass staan.
        //          Implementeert of overerft een update-methode: een methode die ofwel bij het subject de data ophaalt, ofwel deze informatie meegeleverd krijgt
        //  Het is mogelijk dat een klasse de in Java ingebouwde interface EligibleObserverPattern implementeert, dit is een goede aanwijzing dat we te maken hebben met een EligibleObserverPattern.

        // Bevat een referentie naar de Subject, of een van zn subclasses (nodig?)

        ImplementationOrSuperclassFinder implFinder = new ImplementationOrSuperclassFinder();

        for (CompilationUnit compilationUnit : files) {
            for (EligibleObserverPattern observerPattern : eligiblePatterns) {
                implFinder.reset();
                implFinder.visit(compilationUnit, observerPattern.getAbstractObserver().getClassDeclaration());

                List<ClassOrInterfaceDeclaration> classDeclarations = implFinder.getClasses();
            }
        }
    }

    private ObserverPattern makeObserverPattern (EligibleObserverPattern eligibleObserverPattern) {
        ObserverPattern observerPattern = new ObserverPattern();

        // Fill abstract observable
        AbstractObservable abstractObservable = eligibleObserverPattern.getAbstractObservable();
        observerPattern.setAbstractObservable(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(abstractObservable.getClassDeclaration()))
                        .setName(abstractObservable.getResolvedTypeDeclaration().getQualifiedName())
                        .setDeclaration(abstractObservable.getClassDeclaration())
        );

        // Fill abstract observer
        AbstractObservable abstractObserver = eligibleObserverPattern.getAbstractObservable();
        observerPattern.setAbstractObserver(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(abstractObserver.getClassDeclaration()))
                        .setName(abstractObserver.getResolvedTypeDeclaration().getQualifiedName())
                        .setDeclaration(abstractObserver.getClassDeclaration())
        );

        // Fill concrete observable
        List<ConcreteObservable> concreteObservables = eligibleObserverPattern.getConcreteObservables();
        for (ConcreteObservable concreteObservable : concreteObservables) {
            observerPattern.addConcreteObservable(
                    new ClassOrInterface()
                            .setFilePart(FilePartResolver.getFilePartOfNode(concreteObservable.getClassDeclaration()))
                            .setName(concreteObservable.getResolvedTypeDeclaration().getQualifiedName())
                            .setDeclaration(concreteObservable.getClassDeclaration())
            );
        }

        // Fill concrete observer
        List<ConcreteObserver> concreteObservers = eligibleObserverPattern.getConcreteObservers();
        for (ConcreteObserver concreteObserver : concreteObservers) {
            observerPattern.addConcreteObserver(
                    new ClassOrInterface()
                            .setFilePart(FilePartResolver.getFilePartOfNode(concreteObserver.getClassDeclaration()))
                            .setName(concreteObserver.getResolvedTypeDeclaration().getQualifiedName())
                            .setDeclaration(concreteObserver.getClassDeclaration())
            );
        }

        return observerPattern;
    }
}
