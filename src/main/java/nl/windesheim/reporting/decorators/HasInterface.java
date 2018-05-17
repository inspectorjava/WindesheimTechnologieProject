package nl.windesheim.reporting.decorators;

import nl.windesheim.reporting.components.IFoundPatternReport;

/**
 * HasInterface report decorator
 */
public class HasInterface extends FoundPatternReportDecorator{

    /**
     * interface Name
     */
    private String interfaceName;

    /**
     * Default constructor.
     * @param foundPatternReport decorator.
     */
    public HasInterface(final IFoundPatternReport foundPatternReport) {
        super(foundPatternReport);
    }

    /**
     * Set the interface name.
     * @param interfaceName the interface name.
     */
    public void setInterfaceName(final String interfaceName) {
        this.interfaceName = interfaceName;
    }

    /**
     * Return report part for the interface.
     * @return interface report.
     */
    public String getReport() {
        return super.getReport() + " and uses interface: " + this.interfaceName;
    }
}
