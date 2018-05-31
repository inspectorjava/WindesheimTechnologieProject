package nl.windesheim.codeparser.analyzers.strategy;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
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
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;
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

    /**
     * Finds relations between symbols.
     */
    private JavaSymbolSolver javaSymbolSolver;

    /**
     * A solver for data types.
     */
    private CombinedTypeSolver typeSolver;

    /**
     * A finder which searches for implementations of a interface.
     */
    private final ImplementationOrSuperclassFinder implFinder;

    /**
     * A finder which searches for eligible strategy context classes.
     */
    private final EligibleStrategyContextFinder contextFinder;

    /**
     * Init the strategy analyzer.
     */
    public StrategyAnalyzer() {
        super();

        //Create visitors which will find classes with special properties
        contextFinder = new EligibleStrategyContextFinder();
        implFinder = new ImplementationOrSuperclassFinder();
    }

    @Override
    public ArrayList<IDesignPattern> analyze(final List<CompilationUnit> files) {
        typeSolver = getParent().getTypeSolver();

        ArrayList<IDesignPattern> patterns = new ArrayList<>();

        //Without a type solver the strategy analyzer can't function
        if (typeSolver == null) {
            return patterns;
        }

        javaSymbolSolver = new JavaSymbolSolver(typeSolver);

        List<Pair<VariableDeclarator, ClassOrInterfaceDeclaration>> eligibleContexts = findEligibleContexts(files);

        //foreach eligible context class
        for (Pair<VariableDeclarator, ClassOrInterfaceDeclaration> eligibleContext : eligibleContexts) {
            //Get the strategy interface of which the context type has a variable
            ClassOrInterfaceDeclaration strategyInterface = eligibleContext.getValue();

            //Walk up the tree until we have the class containing the variable declaration, this is the context class
            Node currentNode = eligibleContext.getKey();
            while (!(currentNode instanceof ClassOrInterfaceDeclaration)) {
                if (!currentNode.getParentNode().isPresent()) {
                    break;
                }
                currentNode = currentNode.getParentNode().get();
            }

            //If we broke out of the loop above but the current node is not a classOrInterfaceDeclaration
            // we have a issue, so continue to check other eligible contexts
            if (!(currentNode instanceof ClassOrInterfaceDeclaration)) {
                continue;
            }

            ClassOrInterfaceDeclaration context = (ClassOrInterfaceDeclaration) currentNode;

            //Find all method calls in the context class
            List<MethodCallExpr> methodCalls = context.findAll(MethodCallExpr.class);

            boolean strategyCalled = doesClassCallStrategy(methodCalls, strategyInterface);

            //If the context doesn't call the strategy it isn't a working strategy pattern
            if (!strategyCalled) {
                continue;
            }

            List<ClassOrInterfaceDeclaration> strategies = findStrategies(files, strategyInterface);

            //We should at least have one implementation of the strategy interface, else the pattern won't work
            if (strategies.isEmpty()) {
                continue;
            }

            //At this point every requirement has been met so we make the Strategy class
            Strategy strategyPattern = createStrategy(files, context, strategyInterface, strategies);

            patterns.add(strategyPattern);
        }

        return patterns;
    }

    /**
     * Create a strategy pattern object.
     *
     * @param files             the files in which we found the pattern
     * @param context           the context class
     * @param strategyInterface the strategy interface
     * @param strategies        the strategies
     * @return the strategy pattern object
     */
    private Strategy createStrategy(
            final List<CompilationUnit> files,
            final ClassOrInterfaceDeclaration context,
            final ClassOrInterfaceDeclaration strategyInterface,
            final List<ClassOrInterfaceDeclaration> strategies
    ) {
        //At this point every requirement has been met so we make the Strategy class
        Strategy strategyPattern = new Strategy();

        //Resolve the file and part of the file where the context class is defined
        strategyPattern.setContext(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(context))
                        .setName(context.getNameAsString())
                        .setDeclaration(context)
        );

        //Because of the way the strategy interface is found it doesn't have a file linked to it
        // So we need to find a instance which has one
        CompilationUnit newStrategy = FilePartResolver.findCompilationUnitOfNode(files, strategyInterface);

        if (newStrategy != null) {
            //Resolve the file and part of the file where the strategy interface is defined
            strategyPattern.setStrategyInterface(new ClassOrInterface()
                    .setFilePart(FilePartResolver.getFilePartOfNode(newStrategy))
                    .setName(strategyInterface.getNameAsString())
                    .setDeclaration(strategyInterface));
        }

        ArrayList<ClassOrInterface> fileParts = new ArrayList<>();
        for (ClassOrInterfaceDeclaration strategy : strategies) {

            //Resolve the file and part of the file where the strategy is defined
            fileParts.add(new ClassOrInterface()
                    .setFilePart(FilePartResolver.getFilePartOfNode(strategy))
                    .setName(strategy.getNameAsString())
                    .setDeclaration(strategy));
        }

        strategyPattern.setStrategies(fileParts);

        return strategyPattern;
    }

    /**
     * Finds a list of classes which are strategy interface implementations.
     *
     * @param files       the files in which to search
     * @param declaration the strategy interface
     * @return a list of classes
     */
    private List<ClassOrInterfaceDeclaration> findStrategies(
            final List<CompilationUnit> files,
            final ClassOrInterfaceDeclaration declaration
    ) {
        ArrayList<ClassOrInterfaceDeclaration> strategies = new ArrayList<>();

        //For each file
        for (CompilationUnit compilationUnit : files) {
            //Reset the visitor
            implFinder.reset();

            //Visit all nodes
            implFinder.visit(compilationUnit, declaration);

            //Add the buffer to the collection
            strategies.addAll(implFinder.getClasses());

            //Add all errors which were encountered to the list
            for (Exception e : implFinder.getErrors()) {
                addError(e);
            }
        }

        return strategies;
    }

    /**
     * Determins if a context class ever calls a strategy interface.
     *
     * @param methodCalls       list of method calls of the context class
     * @param strategyInterface the strategy interface which should be called
     * @return a boolean
     */
    private boolean doesClassCallStrategy(
            final List<MethodCallExpr> methodCalls,
            final ClassOrInterfaceDeclaration strategyInterface
    ) {

        boolean strategyCalled = false;

        //Loop over the method calls
        for (MethodCallExpr methodCallExpr : methodCalls) {

            //We are looking for a call in the scope of the variable declaration
            if (!methodCallExpr.getScope().isPresent()) {
                continue;
            }

            //Resolve the type of the scope
            ResolvedType scopeType = javaSymbolSolver.calculateType(methodCallExpr.getScope().get());
            if (!(scopeType instanceof ReferenceTypeImpl)) {
                continue;
            }

            ResolvedReferenceTypeDeclaration referenceType
                    = ((ReferenceTypeImpl) scopeType).getTypeDeclaration();
            if (!(referenceType instanceof JavaParserInterfaceDeclaration)) {
                continue;
            }
            //If the scope is the same type as the strategy interface
            if (((JavaParserInterfaceDeclaration) referenceType)
                    .getWrappedNode().equals(strategyInterface)) {
                strategyCalled = true;
            }
        }

        return strategyCalled;
    }

    /**
     * Finds a list of eligible context classes.
     *
     * @param files the files to be searched
     * @return a list of eligible contexts
     */
    private List<Pair<VariableDeclarator, ClassOrInterfaceDeclaration>>
    findEligibleContexts(final List<CompilationUnit> files
    ) {

        List<Pair<VariableDeclarator, ClassOrInterfaceDeclaration>> eligibleContexts = new ArrayList<>();

        //For each file
        for (CompilationUnit compilationUnit : files) {
            //Reset the state of the visitor
            contextFinder.reset();

            //visit all nodes and save eligible contexts in buffer
            contextFinder.visit(compilationUnit, typeSolver);

            //Read buffer into collection
            eligibleContexts.addAll(contextFinder.getClasses());

            for (Exception e : contextFinder.getErrors()) {
                addError(e);
            }
        }

        return eligibleContexts;
    }
}

