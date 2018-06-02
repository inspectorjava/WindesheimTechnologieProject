package nl.windesheim.reporting.builders;

import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.*;

import java.util.List;

/**
 * Command pattern found builder.
 */
public class CommandFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * Files list.
     */
    private final List<String> files;

    /**
     * Command interface.
     */
    private final String commandInterface;

    /**
     * Command list.
     */
    private final  List<String> commands;

    /**
     * Command receivers list.
     */
    private final  List<String> receivers;

    /**
     * Set the required parameters for the builder.
     * @param files the files used to create a command pattern
     * @param commandInterface the interface of the command pattern
     * @param commands the commands.
     * @param receivers the command receivers of the command pattern
     */
    public CommandFoundPatternBuilder(
            final List<String> files,
            final String commandInterface,
            final List<String> commands,
            final List<String> receivers
    ) {
        super();
        this.files = files;
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

        HasFiles hasFiles = new HasFiles(command);
        hasFiles.setFiles(this.files);

        HasInterface hasInterface = new HasInterface(command);
        hasInterface.setInterfaceName(this.commandInterface);

        HasReceivers hasReceivers = new HasReceivers(command);
        hasReceivers.setLinks(this.receivers);

        return hasInterface;
    }
}
