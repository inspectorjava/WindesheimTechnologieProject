package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;

import java.util.List;

/**
 * Command pattern.
 */
public class Command implements IDesignPattern {

    /**
     * The file part which contains the context class of the command pattern.
     */
    private ClassOrInterface context;

    /**
     * The file part which contains the command interface.
     */
    private ClassOrInterface commandInterface;

    /**
     * A list of file command files.
     */
    private List<ClassOrInterface> commands;

    /**
     * @return the context of the command pattern
     */
    public ClassOrInterface getContext() {
        return context;
    }

    /**
     * @param context the context of the command pattern
     * @return this
     */
    public Command setContext(final ClassOrInterface context) {
        this.context = context;
        return this;
    }

    /**
     * @return the command action listener interface of the command pattern
     */
    public ClassOrInterface getCommandInterface() {
        return commandInterface;
    }

    /**
     * @param commandInterface the command action listener of the command pattern
     * @return this
     */
    public Command setCommandInterface(final ClassOrInterface commandInterface) {
        this.commandInterface = commandInterface;
        return this;
    }

    /**
     * @return a list of commands which can be used in this command pattern
     */
    public List<ClassOrInterface> getCommands() {
        return commands;
    }

    /**
     * @param commands a list of commands which can be used in this command pattern
     * @return this
     */
    public Command setCommands(final List<ClassOrInterface> commands) {
        this.commands = commands;
        return this;
    }
}
