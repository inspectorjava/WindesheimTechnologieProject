package marslanden.analyzers;

import com.github.javaparser.ast.CompilationUnit;
import marslanden.patterns.IDesignPattern;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Defines a base for all design pattern analyzers.
 */
public abstract class PatternAnalyzer {
    /**
     * The children for this composite
     */
    private ArrayList<PatternAnalyzer> children = new ArrayList<>();

    /**
     * Adds a child to the list of PatternAnalyzers which makes this composite
     * @param analyzer the pattern analyzer you would like to add
     * @return this object
     */
    public PatternAnalyzer addChild(final PatternAnalyzer analyzer){
        children.add(analyzer);
        return this;
    }

    /**
     * Adds a child to the list of PatternAnalyzers which makes this composite
     * @param analyzer the pattern analyzer you would like to add
     * @return this object
     */
    public PatternAnalyzer removeChild(final PatternAnalyzer analyzer){
        children.remove(analyzer);
        return this;
    }

    /**
     * @return the children of this analyzer
     */
    public ArrayList<PatternAnalyzer> getChildren(){
        return children;
    }

    /**
     * Analyzes design patterns in a set of files and calls it's children.
     * @param files the file that will be analyzed
     * @return a list of DesignPatterns that were found in this file
     */
    public ArrayList<IDesignPattern> analyze(final ArrayList<CompilationUnit> files){
        ArrayList<IDesignPattern> patterns = this.analyzePattern(files);

        for (PatternAnalyzer patternAnalyzer : this.children) {
            patterns.addAll(patternAnalyzer.analyzePattern(files));
        }

        return patterns;
    }

    /**
     * Analyzes design patterns in a set of files.
     * @param files the file that will be analyzed
     * @return a list of DesignPatterns that were found in this file
     */
    protected abstract ArrayList<IDesignPattern> analyzePattern(ArrayList<CompilationUnit> files);
}
