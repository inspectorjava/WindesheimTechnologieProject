package nl.windesheim.codeparser.analyzers;

import com.github.javaparser.ast.CompilationUnit;
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
}
