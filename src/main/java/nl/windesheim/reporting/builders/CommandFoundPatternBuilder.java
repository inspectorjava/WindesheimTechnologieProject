package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.ClassOrInterface;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasCommands;
import nl.windesheim.reporting.decorators.HasInterface;
import nl.windesheim.reporting.decorators.HasReceivers;

import java.util.List;

/**
 * Command pattern found builder.
 */
public class CommandFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * Command interface.
     */
    private final ClassOrInterface commandInterface;

    /**
     * Command list.
     */
    private final  List<ClassOrInterface> commands;

    /**
     * Command receivers list.
     */
    private final  List<ClassOrInterface> receivers;

    /**
     * Set the required parameters for the builder.
     * @param commandInterface the interface of the command pattern
     * @param commands the commands.
     * @param receivers the command receivers of the command pattern
     */
    public CommandFoundPatternBuilder(
            final ClassOrInterface commandInterface,
            final List<ClassOrInterface> commands,
            final List<ClassOrInterface> receivers
    ) {
        super();
        this.commandInterface = commandInterface;
        this.commands = commands;
        this.receivers = receivers;
    }


    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.COMMAND);

        HasCommands command = new HasCommands(patternReport);
        command.setCommands(this.commands);

        HasInterface hasInterface = new HasInterface(command);
        hasInterface.setInterface(this.commandInterface);

        HasReceivers hasReceivers = new HasReceivers(command);
        hasReceivers.setReceivers(this.receivers);

        return hasInterface;
    }
}
