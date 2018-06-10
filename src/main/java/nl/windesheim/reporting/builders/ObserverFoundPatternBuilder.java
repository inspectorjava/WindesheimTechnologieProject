package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.patterns.ObserverPattern;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasClassList;
import nl.windesheim.reporting.decorators.HasClassOrInterface;

/**
 * Strategy pattern found builder.
 */
public class ObserverFoundPatternBuilder extends AbstractFoundPatternBuilder {
    /**
     * The observer pattern.
     */
    private final ObserverPattern pattern;

    /**
     * Set the required parameters for the builder.
     * @param pattern the observer pattern
     */
    public ObserverFoundPatternBuilder(
            final ObserverPattern pattern
    ) {
        super();
        this.pattern = pattern;
    }


    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.OBSERVER);

        HasClassOrInterface aObservable = new HasClassOrInterface(patternReport);
        aObservable.setName("Abstract Observable");
        aObservable.setClassOrInterface(this.pattern.getAbstractObservable());

        HasClassOrInterface aObserver = new HasClassOrInterface(patternReport);
        aObserver.setName("Abstract Observer");
        aObserver.setClassOrInterface(this.pattern.getAbstractObserver());

        HasClassList cObservables = new HasClassList(aObservable);
        cObservables.setName("Concrete Observables");
        cObservables.setClasses(this.pattern.getConcreteObservables());

        HasClassList cObservers = new HasClassList(aObserver);
        cObservers.setName("Concrete Observers");
        cObservers.setClasses(this.pattern.getConcreteObservers());

        return cObservers;
    }
}
