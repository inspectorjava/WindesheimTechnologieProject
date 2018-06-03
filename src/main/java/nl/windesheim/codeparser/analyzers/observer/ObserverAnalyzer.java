package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import javassist.compiler.ast.MethodDecl;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.observer.components.*;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.ObserverPattern;

import java.util.*;

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
        typeSolver = getParent().getTypeSolver();
        javaSymbolSolver = new JavaSymbolSolver(typeSolver);

        // Find abstract observable classes
        AbstractObservableFinder abstractObservableFinder = new AbstractObservableFinder(typeSolver);
        for (CompilationUnit compilationUnit : files) {
            abstractObservableFinder.visit(compilationUnit, null);
        }

        List<EligibleObserverPattern> eligiblePatterns = abstractObservableFinder.getObserverPatterns();

        // Search for classes that extend the abstract observables
        concreteObservableFinder(files, eligiblePatterns);

        // Find abstract observer classes
        AbstractObserverFinder abstractObserverFinder = new AbstractObserverFinder(typeSolver, eligiblePatterns);
        for (CompilationUnit compilationUnit : files) {
            abstractObserverFinder.visit(compilationUnit, null);
        }

        // Search for classes that extend the abstract observers
        concreteObserverFinder(files, eligiblePatterns);

        List<IDesignPattern> patterns = new ArrayList<>();
        for (EligibleObserverPattern eligiblePattern : eligiblePatterns) {
            patterns.add(makeObserverPattern(eligiblePattern));
        }

        for (IDesignPattern pattern : patterns) {
            ObserverPattern observerPattern = (ObserverPattern) pattern;

            System.out.println("Observer pattern");
            System.out.println("\tAbstract observable: " + observerPattern.getAbstractObservable().getName());
            System.out.println("\tConcrete observables (" + observerPattern.getConcreteObservables().size() + ")");
            for (ClassOrInterface c : observerPattern.getConcreteObservables()) {
                System.out.println("\t\t- " + c.getName());
            }

            System.out.println("\tAbstract observer: " + observerPattern.getAbstractObserver().getName());
            System.out.println("\tConcrete observers (" + observerPattern.getConcreteObservers().size() + ")");
            for (ClassOrInterface c : observerPattern.getConcreteObservers()) {
                System.out.println("\t\t- " + c.getName());
            }

            System.out.println();
        }

        return patterns;
    }

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

    private void concreteObserverFinder (List<CompilationUnit> files, List<EligibleObserverPattern> eligiblePatterns) {
        ImplementationOrSuperclassFinder implFinder = new ImplementationOrSuperclassFinder();

        Map<EligibleObserverPattern, List<ClassOrInterfaceDeclaration>> eligibleConcreteObserverMap = new HashMap<>();

        for (CompilationUnit compilationUnit : files) {
            for (EligibleObserverPattern observerPattern : eligiblePatterns) {
                implFinder.reset();
                implFinder.visit(compilationUnit, observerPattern.getAbstractObserver().getClassDeclaration());

                // Check if the class actually implements the update method
                List<ClassOrInterfaceDeclaration> subclassDeclarations = implFinder.getClasses();

                if (!subclassDeclarations.isEmpty()) {
                    if (eligibleConcreteObserverMap.containsKey(observerPattern)) {
                        eligibleConcreteObserverMap.get(observerPattern).addAll(subclassDeclarations);
                    } else {
                        eligibleConcreteObserverMap.put(
                                observerPattern,
                                new ArrayList<>(subclassDeclarations)
                        );
                    }
                }
            }
        }

        for (EligibleObserverPattern observerPattern : eligibleConcreteObserverMap.keySet()) {
            List<ClassOrInterfaceDeclaration> subclassDeclarations = eligibleConcreteObserverMap.get(observerPattern);

            // Check whether the subclasses contain a method with the same signature as the update method in the abstract observer
            for (ClassOrInterfaceDeclaration subclassDeclaration : subclassDeclarations) {
                List<MethodDeclaration> subclassMethods =  subclassDeclaration.getMethods();
                boolean extendsUpdateMethod = false;

                for (MethodDeclaration method : subclassMethods) {
                    ResolvedMethodDeclaration abstractUpdateMethod = observerPattern.getAbstractObserver().getUpdateMethod();
                    if (!(abstractUpdateMethod instanceof JavaParserMethodDeclaration)) {
                        continue;
                    }

                    MethodDeclaration targetMethod = ((JavaParserMethodDeclaration) abstractUpdateMethod).getWrappedNode();
                    CallableDeclaration.Signature targetSignature = targetMethod.getSignature();
                    if (method.getSignature().equals(targetSignature)) {
                        extendsUpdateMethod = true;
                        break;
                    }
                }

                if (extendsUpdateMethod) {
                    observerPattern.addConcreteObserver(new ConcreteObserver(subclassDeclaration));
                }
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
        AbstractObserver abstractObserver = eligibleObserverPattern.getAbstractObserver();
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
