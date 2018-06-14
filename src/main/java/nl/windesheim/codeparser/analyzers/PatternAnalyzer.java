package nl.windesheim.codeparser.analyzers;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.List;

/**
 * Defines a base for all design pattern analyzers.
 */
public abstract class PatternAnalyzer {

    /**
     * A TypeSolver which can be used to find relations between classes when analyzing.
     */
    private TypeSolver typeSolver;

    /**
     * Analyzes design patterns in a set of files.
     *
     * @param files the file that will be analyzed
     * @return a list of DesignPatterns that were found in this file
     */
    public abstract List<IDesignPattern> analyze(List<CompilationUnit> files);

    /**
     * @return A TypeSolver which can be used to find relations between classes.
     */
    protected TypeSolver getTypeSolver() {
        return typeSolver;
    }

    /**
     * @param typeSolver A TypeSolver which can be used to find relations between classes.
     * @return this
     */
    public PatternAnalyzer setTypeSolver(final TypeSolver typeSolver) {
        this.typeSolver = typeSolver;
        return this;
    }
}
