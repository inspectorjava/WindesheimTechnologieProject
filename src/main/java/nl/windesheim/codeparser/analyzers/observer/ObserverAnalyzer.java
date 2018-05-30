package nl.windesheim.codeparser.analyzers.observer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.observer.components.AbstractObservable;
import nl.windesheim.codeparser.patterns.IDesignPattern;

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
        typeSolver = getParent().getTypeSolver();
        javaSymbolSolver = new JavaSymbolSolver(typeSolver);

        AbstractObservableFinder abstractObservableFinder = new AbstractObservableFinder(typeSolver);

        //  Subject
            //  Een Subject is een (abstracte) klasse met de volgende kenmerken:
            //  Bevat een collectie van objecten
            //  Bevat een methode om objecten aan deze collectie toe te voegen (attach)
            //  Bevat een methode om objecten uit deze collectie te verwijderen (detach)
            //  Bevat een notify-methode, een methode waarin voor alle objecten in de collectie een bepaalde methode (update) wordt aangeroepen.
            //  Het is mogelijk dat het subject als een interface is gedefinieerd, in dat geval moeten de attach, detach en notify-methodes door het interface worden afgedwongen, en moeten deze op bovenstaande manier worden geïmplementeerd door de realisaties van de interface.

        for (CompilationUnit compilationUnit : files) {
            abstractObservableFinder.visit(compilationUnit, null);
        }

        List<AbstractObservable> abstractObservables = abstractObservableFinder.getAbstractObservables();

        for (AbstractObservable abstractObservable : abstractObservables) {
            System.out.println("Found abstract observable: " + abstractObservable.getClassDeclaration().getNameAsString()
                    + " which has " + abstractObservable.getObserverCollections().size() + " observer collections");
        }

        //  ConcreteSubject
            //  Als een klasse een uitbreiding is van de Java-klasse Observable, is dit ook een aanknopingspunt om verder te zoeken naar de implementatie van het Observer-patroon. In dit geval wordt de naam die als generic type wordt meegegeven aan de Observable gezien als als typenaam van de Observer-klasse (*checken*).

        //  Observer
            //  Om te weten of we te maken hebben met een observer pattern, moeten we weten of er ook klasses zijn gedefinieerd die als observers dienen. Een observer voldoet aan de volgende kenmerken:
            //  De klasse is van hetzelfde type als de collectie van observers in Subject, of als generic meegegeven aan de klasse Observable (*checken*).
            //  Bevat een update-methode, of dwingt deze af (zie ConcreteObserver).
            //  Bevat een referentie naar de Subject, of een van zn subclasses.

        //  ConcreteObserver
            //  Bevat een referentie naar de Subject of een van zijn subclasses. Deze referentie mag in de superclass staan.
            //          Implementeert een update-methode: een methode die ofwel bij het subject de data ophaalt, ofwel deze informatie meegeleverd krijgt
            //  Het is mogelijk dat een klasse de in Java ingebouwde interface Observer implementeert, dit is een goede aanwijzing dat we te maken hebben met een Observer.

        return new ArrayList<>();
    }
}
