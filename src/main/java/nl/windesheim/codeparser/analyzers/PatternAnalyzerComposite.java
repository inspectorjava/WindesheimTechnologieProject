package nl.windesheim.codeparser.analyzers;

import com.github.javaparser.ast.CompilationUnit;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * The top level composite which doesn't analyze a pattern but only holds other PatternAnalyzers.
 */
public class PatternAnalyzerComposite extends PatternAnalyzer {

    /**
     * The children for this composite.
     */
    private ArrayList<PatternAnalyzer> children = new ArrayList<PatternAnalyzer>();

    /**
     * Adds a child to the list of PatternAnalyzers which makes this composite.
     * @param analyzer the pattern analyzer you would like to add
     * @return this object
     */
    public PatternAnalyzer addChild(final PatternAnalyzer analyzer) {
        children.add(analyzer);
        return this;
    }

    /**
     * Adds a child to the list of PatternAnalyzers which makes this composite.
     * @param analyzer the pattern analyzer you would like to add
     * @return this object
     */
    public PatternAnalyzer removeChild(final PatternAnalyzer analyzer) {
        children.remove(analyzer);
        return this;
    }

    /**
     * @return the children of this analyzer
     */
    public ArrayList<PatternAnalyzer> getChildren() {
        return children;
    }

    /**
     * Analyzes design patterns in a set of files and calls it's children.
     * @param files the file that will be analyzed
     * @return a list of DesignPatterns that were found in this file
     */
    public List<IDesignPattern> analyze(final List<CompilationUnit> files) {
        ArrayList<IDesignPattern> patterns = new ArrayList<>();

        for (PatternAnalyzer patternAnalyzer : this.children) {
            patterns.addAll(patternAnalyzer.analyze(files));
        }

        return patterns;
    }

}
