package nl.windesheim.reporting;

/**
 * Design pattern types allowed thus far.
 */
public enum DesignPatternType {
    /**
     * None pattern.
     */
    NONE("none"),
    /**
     * Singleton pattern.
     */
    SINGLETON("Singleton"),
    /**
     * Strategy pattern.
     */
    STRATEGY("Strategy"),
    /**
     * Command.
     */
    COMMAND("Command"),
    /**
     * Abstract factory pattern.
     */
    ABSTRACT_FACTORY("Abstract Factory"),
    /**
     * Chain of responsibility pattern.
     */
    CHAIN_OF_RESPONSIBILITY("Chain of responsibility"),
    /**
     * Composite pattern.
     */
    COMPOSITE("Composite");

    /**
     * the name to display.
     */
    private String displayName;

    /**
     * @param displayName the name that is displayed
     */
    DesignPatternType(final String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
