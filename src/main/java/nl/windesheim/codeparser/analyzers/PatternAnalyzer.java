package nl.windesheim.codeparser.analyzers;

import com.github.javaparser.ast.CompilationUnit;
import nl.windesheim.codeparser.analyzers.util.ErrorLog;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.List;

/**
 * Defines a base for all design pattern analyzers.
 */
public abstract class PatternAnalyzer {

    /**
     * The parent of a analyzer.
     */
    private PatternAnalyzerComposite parent;

    private ErrorLog errorLog;

    /**
     * A list of errors which were encountered when analyzing.
     */
//    private final List<Exception> errors = new ArrayList<>();

    public PatternAnalyzer () {
        errorLog = new ErrorLog();
    }

    /**
     * Analyzes design patterns in a set of files.
     *
     * @param files the file that will be analyzed
     * @return a list of DesignPatterns that were found in this file
     */
    public abstract List<IDesignPattern> analyze(List<CompilationUnit> files);

    /**
     * @return the parent of this analyzer
     */
    public PatternAnalyzerComposite getParent() {
        return parent;
    }

    /**
     * @param parent the parent of this analyzer
     * @return this
     */
    public PatternAnalyzer setParent(final PatternAnalyzerComposite parent) {
        this.parent = parent;
        return this;
    }

    /**
     * @return The error log wrapper
     */
    protected ErrorLog getErrorLog() {
        return errorLog;
    }

    /**
     * Adds a error to the error log.
     *
     * @param error The error to add
     */
    protected void addError(final Exception error) {
        errorLog.addError(error);
    }

    /**
     * Clears the error log.
     */
    protected void clearErrors() {
        errorLog.clearErrors();
    }

    /**
     * @return The list of errors
     */
    public List<Exception> getErrors() {
        return errorLog.getErrors();
    }
}
