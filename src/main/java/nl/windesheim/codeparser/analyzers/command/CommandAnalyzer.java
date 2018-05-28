package nl.windesheim.codeparser.analyzers.command;

import com.github.javaparser.ast.CompilationUnit;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.List;

/**
 * This analyzer tries to detect a command pattern.
 * A group of classes is seen as a command pattern if the following conditions are true:
 * - There is a interface with a method for the execution for the action listener
 * - The action listener is at least one time implemented
 * - The action listener contains a reference to a receiver outside his own object
 */
public class CommandAnalyzer extends PatternAnalyzer {

    @Override
    public List<IDesignPattern> analyze(List<CompilationUnit> files) {
        return null;
    }

}
