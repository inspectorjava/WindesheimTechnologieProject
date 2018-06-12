package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.patterns.ObserverPattern;
import nl.windesheim.codeparser.patterns.properties.ObserverPatternProperties;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.components.Result;
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

        ObserverPatternProperties patternProps = this.pattern.getPatternProperties();

        int errors = 0;

        if (!patternProps.isObservableHasDetach()) {
            patternReport.addPatternRemark("Observable doesn't have a unsubscribe method");
        } else if (!patternProps.isObservableHasDetach()) {
            patternReport.addPatternRemark("Observer does not unsubscribe itself from Observable");
        }

        if (!patternProps.isObserverHasObservable() && !patternProps.isUpdateHasArguments()) {
            errors += 2;
            patternReport.addPatternError("Observer does not contain a reference to the observable");
        } else if (patternProps.isUpdateHasArguments()) {
            patternReport.addPatternRemark("Arguments are passed to the update method");
        }

        if (!patternProps.isObserverHasAttachCall()) {
            errors++;
            patternReport.addPatternError("Observer does not subscribe itself to Observable");
        }

        if (errors == 1) {
            patternReport.setCertainty(Result.Certainty.LIKELY);
        } else if (errors > 1) {
            patternReport.setCertainty(Result.Certainty.UNLIKELY);
        }

        HasClassOrInterface aObservable = new HasClassOrInterface(patternReport);
        aObservable.setName("Abstract Observable");
        aObservable.setClassOrInterface(this.pattern.getAbstractObservable());

//        HasClassOrInterface aObserver = new HasClassOrInterface(patternReport);
//        aObserver.setName("Abstract Observer");
//        aObserver.setClassOrInterface(this.pattern.getAbstractObserver());

        HasClassList cObservables = new HasClassList(aObservable);
        cObservables.setName("Concrete Observables");
        cObservables.setClasses(this.pattern.getConcreteObservables());

//        HasClassList cObservers = new HasClassList(aObserver);
//        cObservers.setName("Concrete Observers");
//        cObservers.setClasses(this.pattern.getConcreteObservers());

//        return cObservers;
        return cObservables;
    }
}
