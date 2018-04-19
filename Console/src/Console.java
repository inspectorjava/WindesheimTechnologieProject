
/**
 * Main class for the Console project.
 */
public class Console {

    /**
     * Private constructor.
     */
    private Console() {
    }

    /**
     * main function.
     * @param args default arguments
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            System.out.println("Files in argument given.");
        }
        else {
            for (String a : args) {
                // @TODO: Pass this file to Marslanden.
                System.out.println(a);
            }
        }
    }
}
