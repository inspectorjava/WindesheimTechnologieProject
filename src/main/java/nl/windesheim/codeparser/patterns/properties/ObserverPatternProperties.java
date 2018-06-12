package nl.windesheim.codeparser.patterns.properties;

/**
 * Encapsulates optional properties of an observer pattern.
 */
public class ObserverPatternProperties {
    /**
     * Whether the subject contains a detach method.
     */
    private boolean subjectHasDetach;

    /**
     * Whether the observer has a reference to a subject.
     */
    private boolean obsHasSubject;

    /**
     * Whether the observer calls the attach method.
     */
    private boolean obsHasAttachCall;

    /**
     * Whether the observer calls the detach method.
     */
    private boolean obsHasDetachCall;

    /**
     * Whether the update method takes parameters.
     */
    private boolean updateHasArgs;

    /**
     * ObserverPatternProperties constructor.
     */
    public ObserverPatternProperties() {
        subjectHasDetach = false;
        obsHasSubject = false;
        obsHasDetachCall = false;
        obsHasAttachCall = false;
    }

    /**
     * @return Whether the subject contains a detach method
     */
    public boolean isSubjectHasDetach() {
        return subjectHasDetach;
    }

    /**
     * @param subjectHasDetach Whether the subject contains a detach method
     * @return this
     */
    public ObserverPatternProperties setSubjectHasDetach(final boolean subjectHasDetach) {
        if (!this.subjectHasDetach) {
            this.subjectHasDetach = subjectHasDetach;
        }

        return this;
    }

    /**
     * @return Whether the observer has a reference to a subject
     */
    public boolean isObserverHasSubject() {
        return obsHasSubject;
    }

    /**
     * @param obsHasSubject Whether the observer has a reference to a subject
     * @return this
     */
    public ObserverPatternProperties setObserverHasSubject(final boolean obsHasSubject) {
        if (!this.obsHasSubject) {
            this.obsHasSubject = obsHasSubject;
        }

        return this;
    }

    /**
     * @return Whether the observer calls the attach method
     */
    public boolean isObserverHasAttachCall() {
        return obsHasAttachCall;
    }

    /**
     * @param obsHasAttachCall Whether the observer calls the attach method
     * @return this
     */
    public ObserverPatternProperties setObserverHasAttachCall(final boolean obsHasAttachCall) {
        if (!this.obsHasAttachCall) {
            this.obsHasAttachCall = obsHasAttachCall;
        }

        return this;
    }

    /**
     * @return Whether the observer calls the detach method
     */
    public boolean isObserverHasDetachCall() {
        return obsHasDetachCall;
    }

    /**
     * @param obsHasDetachCall Whether the observer calls the detach method
     * @return this
     */
    public ObserverPatternProperties setObserverHasDetachCall(final boolean obsHasDetachCall) {
        if (!this.obsHasDetachCall) {
            this.obsHasDetachCall = obsHasDetachCall;
        }

        return this;
    }

    /**
     * @return Whether the update method takes parameters
     */
    public boolean isUpdateHasArguments() {
        return updateHasArgs;
    }

    /**
     * @param updateHasArgs Whether the update method takes parameters
     * @return this
     */
    public ObserverPatternProperties setUpdateHasArguments(final boolean updateHasArgs) {
        this.updateHasArgs = updateHasArgs;
        return this;
    }
}
