package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.patterns.Command;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasClassList;
import nl.windesheim.reporting.decorators.HasClassOrInterface;

/**
 * Command pattern found builder.
 */
public class CommandFoundPatternBuilder extends AbstractFoundPatternBuilder {

    /**
     * The command pattern.
     */
    private final Command pattern;

    /**
     * Set the required parameters for the builder.
     * @param pattern the command pattern
     */
    public CommandFoundPatternBuilder(
            final Command pattern
    ) {
        super();
        this.pattern = pattern;
    }


    @Override
    public IFoundPatternReport buildReport() {
        FoundPatternReport patternReport = new FoundPatternReport();
        patternReport.setDesignPatternType(DesignPatternType.COMMAND);

        HasClassList command = new HasClassList(patternReport);
        command.setName("Commands");
        command.setClasses(this.pattern.getCommands());

        HasClassOrInterface hasInterface = new HasClassOrInterface(command);
        hasInterface.setName("Command parent");
        hasInterface.setClassOrInterface(this.pattern.getCommandParent());

        HasClassList hasReceivers = new HasClassList(command);
        hasReceivers.setName("Receivers");
        hasReceivers.setClasses(this.pattern.getReceivers());

        return hasInterface;
    }
}
