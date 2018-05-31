package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;

import java.util.List;

/**
 * Command pattern.
 */
public class Command implements IDesignPattern {

    /**
     * The file part which contains the command interface.
     */
    private ClassOrInterface commandParent;

    /**
     * A list of file command files.
     */
    private List<ClassOrInterface> commands;

    /**
     * A list of file who receives the command execution.
     */
    private List<ClassOrInterface> receivers;

    /**
     * @return the definition of the command method.
     */
    public ClassOrInterface getCommandParent() {
        return commandParent;
    }

    /**
     * @param commandParent the definition of the command method.
     * @return this
     */
    public Command setCommandParent(final ClassOrInterface commandParent) {
        this.commandParent = commandParent;
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

    /**
     * @return a list of file who receives the command execution.
     */
    public List<ClassOrInterface> getReceivers() {
        return receivers;
    }

    /**
     * @param receivers a list of file who receives the command execution.
     * @return this
     */
    public Command setReceivers(final List<ClassOrInterface> receivers) {
        this.receivers = receivers;
        return this;
    }
}
