package nl.windesheim.codeparser.analyzers.composite;

import com.github.javaparser.ast.CompilationUnit;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Find a objects with lists of their own objects.
 */
public class CompositeAnalyzer extends PatternAnalyzer {

    @Override
    public List<IDesignPattern> analyze(final List<CompilationUnit> files) {
        for (CompilationUnit file : files) {
        }

        return new ArrayList();
    }
}
