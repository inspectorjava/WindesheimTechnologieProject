package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.*;

import java.util.List;

/**
 * Strategy pattern found builder.
 */
public class ObserverFoundPatternBuilder extends AbstractFoundPatternBuilder {

    private final ClassOrInterface aObservable;

    private final List<ClassOrInterface> cObservables;

    private final ClassOrInterface aObserver;

    private final List<ClassOrInterface> cObservers;

    /**
     * Set the required parameters for the builder.
     * @param aObservable
     * @param cObservables
     * @param aObserver
     * @param cObservers
     */
    public ObserverFoundPatternBuilder(
            final ClassOrInterface aObservable,
            final List<ClassOrInterface> cObservables,
            final ClassOrInterface aObserver,
            final List<ClassOrInterface> cObservers
    ) {
        super();
        this.aObservable = aObservable;
        this.cObservables = cObservables;
        this.aObserver = aObserver;
        this.cObservers = cObservers;
    }


    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.OBSERVER);

        HasAbstractObservable aObservable = new HasAbstractObservable(patternReport);
        aObservable.setAbstractObservable(this.aObservable);

        HasAbstractObserver aObserver = new HasAbstractObserver(patternReport);
        aObserver.setAbstractObserver(this.aObserver);

        HasConcreteObservables cObservables = new HasConcreteObservables(aObservable);
        cObservables.setConcreteObservables(this.cObservables);

        HasConcreteObservers cObservers = new HasConcreteObservers(aObserver);
        cObservers.setConcreteObservers(this.cObservers);

        return cObservers;
    }
}
