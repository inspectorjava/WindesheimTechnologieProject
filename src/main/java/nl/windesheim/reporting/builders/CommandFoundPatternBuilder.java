package nl.windesheim.reporting.builders;

import nl.windesheim.codeparser.patterns.Command;
import nl.windesheim.reporting.DesignPatternType;
import nl.windesheim.reporting.components.AbstractFoundPatternBuilder;
import nl.windesheim.reporting.components.FoundPatternReport;
import nl.windesheim.reporting.components.IFoundPatternReport;
import nl.windesheim.reporting.decorators.HasClassList;
import nl.windesheim.reporting.decorators.HasInterface;
import nl.windesheim.reporting.decorators.HasReceivers;

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

        HasInterface hasInterface = new HasInterface(command);
        hasInterface.setInterface(this.pattern.getCommandParent());

        HasReceivers hasReceivers = new HasReceivers(command);
        hasReceivers.setReceivers(this.pattern.getReceivers());

        return hasInterface;
    }
}
