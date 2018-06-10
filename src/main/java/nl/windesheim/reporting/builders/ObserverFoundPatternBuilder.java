package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.patterns.ObserverPattern;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasAbstractObservable;
import nl.windesheim.reporting.decorators.HasAbstractObserver;
import nl.windesheim.reporting.decorators.HasConcreteObservables;
import nl.windesheim.reporting.decorators.HasConcreteObservers;

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

        HasAbstractObservable aObservable = new HasAbstractObservable(patternReport);
        aObservable.setAbstractObservable(this.pattern.getAbstractObservable());

        HasAbstractObserver aObserver = new HasAbstractObserver(patternReport);
        aObserver.setAbstractObserver(this.pattern.getAbstractObserver());

        HasConcreteObservables cObservables = new HasConcreteObservables(aObservable);
        cObservables.setConcreteObservables(this.pattern.getConcreteObservables());

        HasConcreteObservers cObservers = new HasConcreteObservers(aObserver);
        cObservers.setConcreteObservers(this.pattern.getConcreteObservers());

        return cObservers;
    }
}
