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
    @SuppressWarnings("PMD.ConfusingTernary")
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.OBSERVER);

        ObserverPatternProperties patternProps = this.pattern.getPatternProperties();

        int errors = 0;

        if (!patternProps.isSubjectHasDetach()) {
            patternReport.addPatternRemark("Subject doesn't have a unsubscribe method");
        } else if (!patternProps.isObserverHasDetachCall()) {
            patternReport.addPatternRemark("Observer does not unsubscribe itself from Subject");
        }

        if (patternProps.isUpdateHasArguments()) {
            patternReport.addPatternRemark("Arguments are passed to the update method");
        } else if (!patternProps.isObserverHasSubject() && !patternProps.isUpdateHasArguments()) {
            errors += 2;
            patternReport.addPatternError("Observer does not contain a reference to the subject");
        }

        if (!patternProps.isObserverHasAttachCall()) {
            errors++;
            patternReport.addPatternError("Observer does not subscribe itself to Subject");
        }

        if (errors == 1) {
            patternReport.setCertainty(Result.Certainty.LIKELY);
        } else if (errors > 1) {
            patternReport.setCertainty(Result.Certainty.UNLIKELY);
        }


        HasClassOrInterface abstractSubject = new HasClassOrInterface(patternReport);
        abstractSubject.setName("Abstract Subject");
        abstractSubject.setClassOrInterface(this.pattern.getAbstractSubject());

        HasClassOrInterface abstractObserver = new HasClassOrInterface(abstractSubject);
        abstractObserver.setName("Abstract Observer");
        abstractObserver.setClassOrInterface(this.pattern.getAbstractObserver());

        HasClassList concreteSubjects = new HasClassList(abstractObserver);
        concreteSubjects.setName("Concrete Subjects");
        concreteSubjects.setClasses(this.pattern.getConcreteSubjects());

        HasClassList concreteObservers = new HasClassList(concreteSubjects);
        concreteObservers.setName("Concrete Observers");
        concreteObservers.setClasses(this.pattern.getConcreteObservers());

        return concreteObservers;
    }
}
