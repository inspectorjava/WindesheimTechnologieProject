package nl.windesheim.reporting.components;

/**
 * Result of a found pattern.
 */
public class Result {

    /**
     * Result of found pattern.
     */
    private Certainty result;

    /**
     * Certainty of found pattern.
     */
    public enum Certainty {
        /**
         * The pattern is certainly found.
         */
        CERTAIN,
        /**
         * The pattern is likely found.
         */
        LIKELY,
        /**
         * The pattern is unlikely found.
         */
        UNLIKELY,
        /**
         * The pattern is not found.
         */
        NOT
    }

    /**
     * Set the certainty.
     * @param certainty enum.
     */
    public void setCertainty(final Certainty certainty) {
        this.result = certainty;
    }

    /**
     * Return the result as a string.
     * @return string of result
     */
    public String toString() {
        return this.result.toString();
    }

}
