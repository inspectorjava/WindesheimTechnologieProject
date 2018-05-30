package nl.windesheim.codeparser.analyzers;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a base for all design pattern analyzers.
 */
public abstract class PatternAnalyzer {

    /**
     * The parent of a analyzer.
     */
    private PatternAnalyzerComposite parent;

    /**
     * A list of errors which were encountered when analyzing.
     */
    private final List<Exception> errors = new ArrayList<>();

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
     * Adds a error to the error list.
     *
     * @param error the error to add
     */
    protected void addError(final Exception error) {
        errors.add(error);
    }

    /**
     * Clears the errors in the error list.
     */
    protected void clearErrors() {
        errors.clear();
    }

    /**
     * @return the list of error found while analyzing
     */
    public List<Exception> getErrors() {
        return errors;
    }
}