package marslanden.analyzers;

import com.github.javaparser.ast.CompilationUnit;
import marslanden.patterns.IDesignPattern;

import java.util.ArrayList;

/**
 * The top level composite which doesn't analyze a pattern but only holds other PatternAnalyzers.
 */
public class PatternAnalyzerComposite extends PatternAnalyzer {

    /**
     * @inheritDoc
     */
    @Override
    protected ArrayList<IDesignPattern> analyzePattern(final ArrayList<CompilationUnit> files) {
        return new ArrayList<>();
    }


}
