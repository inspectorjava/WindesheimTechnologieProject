package marslanden;

import parser.ParserWrapper;

/**
 * Main class for the marslanden project.
 */
public final class Main {

    /**
     * Private constructor.
     */
    private Main() {
    }

    /**
     * main function.
     * @param args default arguments
     */
    public static void main(final String[] args) {
        System.out.println("[marslanden] running");

        ParserWrapper pw = new ParserWrapper();
        pw.test();

        System.out.println("[marslanden] done");
    }

}
