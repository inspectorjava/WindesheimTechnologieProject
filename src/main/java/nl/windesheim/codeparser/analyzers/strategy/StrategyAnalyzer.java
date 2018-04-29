package nl.windesheim.codeparser.analyzers.strategy;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserInterfaceDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import javafx.util.Pair;
import nl.windesheim.codeparser.FilePart;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationFinder;
import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * This analyzer tries to detect a strategy pattern.
 * A group of classes is seen as a strategy pattern if the following conditions are true:
 * - There is a class with a attribute with the same type as the strategy interface (context)
 * - The 'context' class has a setter (and getter) for the strategy attribute
 * - The 'context' class has a function which calls a function on the 'strategy interface' attribute
 * - There is a interface which is implemented by all strategies with at least one function (strategy interface)
 * - There is at least one class which implements the strategy interface (strategy)
 */
public class StrategyAnalyzer extends PatternAnalyzer{

    @Override
    public ArrayList<IDesignPattern> analyze(final ArrayList<CompilationUnit> files) {
        CombinedTypeSolver typeSolver = getParent().getTypeSolver();
        ArrayList<IDesignPattern> patterns = new ArrayList<>();

        JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(typeSolver);

        EligibleStrategyContextFinder strategyContextFinder = new EligibleStrategyContextFinder();
        ImplementationFinder implementationFinder = new ImplementationFinder();

        //Without a type solver the strategy analyzer can't function
        if (typeSolver == null) {
            return patterns;
        }

        ArrayList<Pair<VariableDeclarator, ClassOrInterfaceDeclaration>> eligibleContexts = new ArrayList<>();

        //For each file
        for (CompilationUnit compilationUnit : files){
            strategyContextFinder.reset();
            strategyContextFinder.visit(compilationUnit, typeSolver);
            eligibleContexts.addAll(strategyContextFinder.getClasses());
        }

        for (Pair<VariableDeclarator, ClassOrInterfaceDeclaration> eligibleContext : eligibleContexts){
            ClassOrInterfaceDeclaration strategyInterface = eligibleContext.getValue();

            VariableDeclarator strategyVariable = eligibleContext.getKey();
            ClassOrInterfaceType strategyInterfaceType = (ClassOrInterfaceType) strategyVariable.getType();

            ClassOrInterfaceDeclaration context = (ClassOrInterfaceDeclaration) strategyVariable.getParentNode().get().getParentNode().get();

            List<MethodCallExpr> methodCalls = context.findAll(MethodCallExpr.class);

            boolean classCallsStrategy = false;

            for (MethodCallExpr methodCallExpr : methodCalls){
                if (methodCallExpr.getScope().isPresent()){
                    ResolvedType methodType = javaSymbolSolver.calculateType(methodCallExpr.getScope().get());
                    if (methodType instanceof ReferenceTypeImpl) {
                        ResolvedReferenceTypeDeclaration scopeResolvedReferenceType
                                = ((ReferenceTypeImpl) methodType).getTypeDeclaration();
                        if (scopeResolvedReferenceType instanceof JavaParserInterfaceDeclaration) {
                            if (((JavaParserInterfaceDeclaration) scopeResolvedReferenceType).getWrappedNode().equals(strategyInterface)) {
                                classCallsStrategy = true;
                            }
                        }
                    }
                }
            }

            //If the context doesn't call the strategy it isn't a working strategy pattern
            if (!classCallsStrategy) {
                continue;
            }

            ArrayList<ClassOrInterfaceDeclaration> strategies = new ArrayList<>();

            //For each file
            for (CompilationUnit compilationUnit : files){
                implementationFinder.reset();
                implementationFinder.visit(compilationUnit, strategyInterfaceType);
                strategies.addAll(implementationFinder.getClasses());
            }

            if (strategies.size() == 0){
                continue;
            }

            Strategy strategyPattern = new Strategy();

            strategyPattern.setContext(FilePartResolver.getFilePartOfNode(context));

            strategyPattern.setStrategyInterface(FilePartResolver.getFilePartOfNode(strategyInterface));

            ArrayList<FilePart> strategiesFileParts = new ArrayList<>();
            for (ClassOrInterfaceDeclaration strategy : strategies){
                strategiesFileParts.add(FilePartResolver.getFilePartOfNode(strategy));
            }

            strategyPattern.setStrategies(strategiesFileParts);

            patterns.add(strategyPattern);
        }

        return patterns;
    }
}

