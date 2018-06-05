package nl.windesheim.codeparser.analyzers.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for the error log.
 */
public class ErrorLog {
    /**
     * The list of errors.
     */
    private final List<Exception> errors;

    /**
     * ErrorLog constructor.
     */
    public ErrorLog() {
        errors = new ArrayList<>();
    }

    /**
     * Adds a error to the error log.
     *
     * @param error The error to add
     */
    public void addError(final Exception error) {
        errors.add(error);
    }

    /**
     * Clears the error log.
     */
    public void clearErrors() {
        errors.clear();
    }

    /**
     * @return The list of errors
     */
    public List<Exception> getErrors() {
        return errors;
    }
}
