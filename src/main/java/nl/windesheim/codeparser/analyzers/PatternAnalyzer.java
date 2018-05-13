package nl.windesheim.codeparser.analyzers;

import com.github.javaparser.ast.CompilationUnit;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.List;

/**
 * Defines a base for all design pattern analyzers.
 */
public abstract class PatternAnalyzer {

    /**
     * Analyzes design patterns in a set of files.
     * @param files the file that will be analyzed
     * @return a list of DesignPatterns that were found in this file
     */
    public abstract List<IDesignPattern> analyze(List<CompilationUnit> files);

}
