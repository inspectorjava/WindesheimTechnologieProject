package nl.windesheim.codeparser.analyzers.command;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.codeparser.analyzers.PatternAnalyzer;
import nl.windesheim.codeparser.analyzers.util.FilePartResolver;
import nl.windesheim.codeparser.analyzers.util.visitor.EligibleCommonParentFinder;
import nl.windesheim.codeparser.analyzers.util.visitor.ImplementationOrSuperclassFinder;
import nl.windesheim.codeparser.patterns.Command;
import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * This analyzer tries to detect a command pattern.
 * A group of classes is seen as a command pattern if the following conditions are true:
 * - There is a interface or abstract class with minimal one method for the execution of the action
 * - The execution method of the command is at least implemented on one class
 * - The command class contains a reference to a receiver
 * - The command execute method executes a method of the receiver
 * - The commands are defined in a list
 */
public class CommandAnalyzer extends PatternAnalyzer {

    /**
     * Finds eligible common parents.
     */
    private final EligibleCommonParentFinder parentFinder;

    /**
     * A solver for data types.
     */
    private CombinedTypeSolver typeSolver;

    /**
     * A parentFinder which searches for implementations of a interface.
     */
    private final ImplementationOrSuperclassFinder implFinder;

    /**
     * Command analyzer constructor.
     */
    public CommandAnalyzer() {
        super();

        parentFinder = new EligibleCommonParentFinder();
        implFinder = new ImplementationOrSuperclassFinder();
    }

    @Override
    public List<IDesignPattern> analyze(final List<CompilationUnit> files) {
        clearErrors();

        typeSolver = getParent().getTypeSolver();

        ArrayList<IDesignPattern> patterns = new ArrayList<IDesignPattern>();

        // Without a type solver the command analyzer can't function.
        if (typeSolver == null) {
            return patterns;
        }

        // Get a list of classes which could be 'common parent' classes or interfaces.
        List<ClassOrInterfaceDeclaration> parents = findEligibleParents(files);

        for (ClassOrInterfaceDeclaration commandDefinition : parents) {
            List<ClassOrInterfaceDeclaration> implementations = findLinksOfCommonParent(commandDefinition, files);

            // The interface isn't implemented on classes, so it isn't a valid design pattern.
            if (implementations.isEmpty()) {
                continue;
            }

            List<ClassOrInterfaceDeclaration> commands = new ArrayList<>();
            List<ClassOrInterfaceDeclaration> receivers = new ArrayList<>();

            // Check if methods of the parent contains a reference to the receiver.
            for (ClassOrInterfaceDeclaration implementation : implementations) {
                commands.add(implementation);

                // Get the methods from the interface.
                for (MethodDeclaration methodDefinition : commandDefinition.getMethods()) {
                    String methodName = methodDefinition.getNameAsString();
                    List<MethodDeclaration> declarations = implementation.getMethodsByName(methodName);

                    // TODO Check for the definition reference inside the declarations.
                }
            }

            // TODO Test if the receives aren't empty.

            Command command = createCommandPattern(commandDefinition, commands, receivers);
            patterns.add(command);
        }

        return patterns;
    }

    /**
     * Create the command pattern definition.
     *
     * @param commandParent Command pattern definition.
     * @param commands      List of commands.
     * @param receivers     List with the receivers.
     * @return Command pattern definition.
     */
    private Command createCommandPattern(
            final ClassOrInterfaceDeclaration commandParent,
            final List<ClassOrInterfaceDeclaration> commands,
            final List<ClassOrInterfaceDeclaration> receivers
    ) {
        // At this point every requirement has been found to create a command pattern.
        Command commandPattern = new Command();

        commandPattern.setCommandParent(
                new ClassOrInterface()
                        .setFilePart(FilePartResolver.getFilePartOfNode(commandParent))
                        .setName(commandParent.getNameAsString())
                        .setDeclaration(commandParent)
        );

        ArrayList<ClassOrInterface> commandParts = new ArrayList<>();
        for (ClassOrInterfaceDeclaration command : commands) {

            // Resolve the file and part of the file where the command is defined.
            commandParts.add(new ClassOrInterface()
                    .setFilePart(FilePartResolver.getFilePartOfNode(command))
                    .setName(command.getNameAsString())
                    .setDeclaration(command));
        }
        commandPattern.setCommands(commandParts);

        ArrayList<ClassOrInterface> receiverParts = new ArrayList<>();
        for (ClassOrInterfaceDeclaration receiver : receivers) {

            // Resolve the file and part of the file where the receiver is defined.
            receiverParts.add(new ClassOrInterface()
                    .setFilePart(FilePartResolver.getFilePartOfNode(receiver))
                    .setName(receiver.getNameAsString())
                    .setDeclaration(receiver));
        }
        commandPattern.setReceivers(receiverParts);

        return commandPattern;
    }

    /**
     * Finds eligible 'common parent' classes or interfaces.
     *
     * @param files the files in which we want to find eligible parents
     * @return a list of eligible 'common parent' classes
     */
    private List<ClassOrInterfaceDeclaration> findEligibleParents(final List<CompilationUnit> files) {

        //For each file call the parentFinder
        List<ClassOrInterfaceDeclaration> parents = new ArrayList<>();
        for (CompilationUnit compilationUnit : files) {
            parentFinder.reset();
            parentFinder.visit(compilationUnit, typeSolver);
            parents.addAll(parentFinder.getClasses());
        }

        return parents;
    }

    /**
     * Finds 'links' of a 'common parent'.
     *
     * @param parent the 'common parent' for which we are searching 'links'
     * @param files  the files in which we wan't to find the links
     * @return a list of found 'links'
     */
    private List<ClassOrInterfaceDeclaration> findLinksOfCommonParent(
            final ClassOrInterfaceDeclaration parent,
            final List<CompilationUnit> files
    ) {
        ArrayList<ClassOrInterfaceDeclaration> links = new ArrayList<>();

        for (CompilationUnit compilationUnit : files) {
            implFinder.reset();
            implFinder.visit(compilationUnit, parent);
            links.addAll(implFinder.getClasses());

            for (Exception e : implFinder.getErrors()) {
                addError(e);
            }
        }

        return links;
    }

}