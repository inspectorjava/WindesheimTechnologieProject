package nl.windesheim.codeparser.analyzers.abstractfactory;

import com.github.javaparser.ast.CompilationUnit;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.List;

public class AbstractFactoryAnalyzer extends PatternAnalyzer {
    @Override
    public List<IDesignPattern> analyze(List<CompilationUnit> files) {
        return null;
    }
}
