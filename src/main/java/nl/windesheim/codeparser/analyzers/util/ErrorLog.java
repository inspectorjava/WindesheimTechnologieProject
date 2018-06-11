package nl.windesheim.codeparser.analyzers.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for the error log.
 */
public final class ErrorLog {
    /**
     * Singleton instance of ErrorLog.
     */
    private static ErrorLog instance;

    /**
     * The list of errors.
     */
    private final List<Exception> errors;

    /**
     * ErrorLog constructor.
     */
    private ErrorLog() {
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

    /**
     * @return Instance of ErrorLog.
     */
    public static ErrorLog getInstance() {
        if (instance == null) {
            instance = new ErrorLog();
        }

        return instance;
    }
}
