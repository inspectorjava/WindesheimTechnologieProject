package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasAbstractObservable;
import nl.windesheim.reporting.decorators.HasAbstractObserver;
import nl.windesheim.reporting.decorators.HasConcreteObservables;
import nl.windesheim.reporting.decorators.HasConcreteObservers;

import java.util.List;

/**
 * Strategy pattern found builder.
 */
public class ObserverFoundPatternBuilder extends AbstractFoundPatternBuilder {
    /**
     * The file part which contains the abstract observable class or interface.
     */
    private final ClassOrInterface aObservable;

    /**
     * The file part which contains the abstract observer class or interface.
     */
    private final List<ClassOrInterface> cObservables;

    /**
     * A list of file parts which contain concrete observable classes.
     */
    private final ClassOrInterface aObserver;

    /**
     * A list of file parts which contain concrete observer classes.
     */
    private final List<ClassOrInterface> cObservers;

    /**
     * Set the required parameters for the builder.
     * @param aObservable  The abstract observable
     * @param cObservables The concrete observables
     * @param aObserver    The abstract observer
     * @param cObservers   The concrete observers
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
