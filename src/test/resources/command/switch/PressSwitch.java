/**
 * Source: https://en.wikipedia.org/wiki/Command_pattern
 */

/* The test class or client */
public class PressSwitch {
    public static void main(final String[] arguments){
        // Check number of arguments
        if (arguments.length != 1) {
            System.err.println("Argument \"ON\" or \"OFF\" is required.");
            System.exit(-1);
        }

        final Light lamp = new Light();

        final Command switchUp = new FlipUpCommand(lamp);
        final Command switchDown = new FlipDownCommand(lamp);

        final Switch mySwitch = new Switch();

        switch(arguments[0]) {
            case "ON":
                mySwitch.storeAndExecute(switchUp);
                break;
            case "OFF":
                mySwitch.storeAndExecute(switchDown);
                break;
            default:
                System.err.println("Argument \"ON\" or \"OFF\" is required.");
                System.exit(-1);
        }
    }
}