package nl.windesheim.codeparser.analyzers.strategy;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
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
import nl.windesheim.codeparser.ClassOrInterface;
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
public class StrategyAnalyzer extends PatternAnalyzer {

    @Override
    public ArrayList<IDesignPattern> analyze(final List<CompilationUnit> files) {
        CombinedTypeSolver typeSolver = getParent().getTypeSolver();
        ArrayList<IDesignPattern> patterns = new ArrayList<>();

        JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(typeSolver);

        //Create visitors which will find classes with special properties
        EligibleStrategyContextFinder strategyContextFinder = new EligibleStrategyContextFinder();
        ImplementationFinder implementationFinder = new ImplementationFinder();

        //Without a type solver the strategy analyzer can't function
        if (typeSolver == null) {
            return patterns;
        }

        ArrayList<Pair<VariableDeclarator, ClassOrInterfaceDeclaration>> eligibleContexts = new ArrayList<>();

        //For each file
        for (CompilationUnit compilationUnit : files) {
            //Reset the state of the visitor
            strategyContextFinder.reset();

            //visit all nodes and save eligible contexts in buffer
            strategyContextFinder.visit(compilationUnit, typeSolver);

            //Read buffer into collection
            eligibleContexts.addAll(strategyContextFinder.getClasses());
        }

        //foreach eligible context class
        for (Pair<VariableDeclarator, ClassOrInterfaceDeclaration> eligibleContext : eligibleContexts) {
            //Get the strategy interface of which the context type has a variable
            ClassOrInterfaceDeclaration strategyInterface = eligibleContext.getValue();

            //Get the variable deceleration
            VariableDeclarator strategyVariable = eligibleContext.getKey();
            ClassOrInterfaceType strategyInterfaceType = (ClassOrInterfaceType) strategyVariable.getType();

            //Walk up the tree until we have the class containing the variable deceleration, this is the context class
            Node currentNode = strategyVariable;
            while (!(currentNode instanceof ClassOrInterfaceDeclaration)) {
                if (!currentNode.getParentNode().isPresent()) {
                    break;
                }
                currentNode = currentNode.getParentNode().get();
            }

            //If we stopped but the current node is not a class we have a issue, so continue loop
            if (!(currentNode instanceof ClassOrInterfaceDeclaration)) {
                continue;
            }

            ClassOrInterfaceDeclaration context = (ClassOrInterfaceDeclaration) currentNode;

            //Find all method calls in the context class
            List<MethodCallExpr> methodCalls = context.findAll(MethodCallExpr.class);

            boolean classCallsStrategy = false;

            //Loop over the method calls
            for (MethodCallExpr methodCallExpr : methodCalls) {

                //We are looking for a call in the scope of the variable deceleration
                if (methodCallExpr.getScope().isPresent()) {

                    //Resolve the type of the scope
                    ResolvedType scopeType = javaSymbolSolver.calculateType(methodCallExpr.getScope().get());
                    if (scopeType instanceof ReferenceTypeImpl) {
                        ResolvedReferenceTypeDeclaration scopeResolvedReferenceType
                                = ((ReferenceTypeImpl) scopeType).getTypeDeclaration();
                        if (scopeResolvedReferenceType instanceof JavaParserInterfaceDeclaration) {

                            //If the scope is the same type as the strategy interface
                            if (((JavaParserInterfaceDeclaration) scopeResolvedReferenceType)
                                    .getWrappedNode().equals(strategyInterface)) {
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
            for (CompilationUnit compilationUnit : files) {
                //Reset the visitor
                implementationFinder.reset();

                //Visit all nodes
                implementationFinder.visit(compilationUnit, strategyInterfaceType);

                //Add the buffer to the collection
                strategies.addAll(implementationFinder.getClasses());
            }

            //We should at least have one implementation of the strategy interface, else the pattern won't work
            if (strategies.size() == 0) {
                continue;
            }

            //At this point every requirement has been met so we make the Strategy class

            Strategy strategyPattern = new Strategy();

            //Resolve the file and part of the file where the context class is defined
            strategyPattern.setContext(
                    new ClassOrInterface()
                            .setFilePart(FilePartResolver.getFilePartOfNode(context))
                            .setName(context.getNameAsString())
                            .setDeceleration(context)
            );

            //Because of the way the strategy interface is found it doesn't have a file linked to it
            // So we need to find a instance which has one
            CompilationUnit newStrategyInterface = FilePartResolver.findCompilationUnitOfNode(files, strategyInterface);

            if (newStrategyInterface != null) {
                //Resolve the file and part of the file where the strategy interface is defined
                strategyPattern.setStrategyInterface(new ClassOrInterface()
                            .setFilePart(FilePartResolver.getFilePartOfNode(newStrategyInterface))
                            .setName(strategyInterface.getNameAsString())
                            .setDeceleration(strategyInterface));
            }

            ArrayList<ClassOrInterface> strategiesFileParts = new ArrayList<>();
            for (ClassOrInterfaceDeclaration strategy : strategies) {

                //Resolve the file and part of the file where the strategy is defined
                strategiesFileParts.add(new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(strategy))
                        .setName(strategy.getNameAsString())
                        .setDeceleration(strategy));
            }

            strategyPattern.setStrategies(strategiesFileParts);

            patterns.add(strategyPattern);
        }

        return patterns;
    }
}

