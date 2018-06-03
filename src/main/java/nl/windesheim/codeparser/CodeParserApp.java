package nl.windesheim.codeparser;

import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * CodeParserApp for console parser.
 */
public final class CodeParserApp {
    /**
     * Private constructor.
     */
    private CodeParserApp() {
        System.out.println("[marslanden] running");
        System.out.println("[marslanden] done");
    }

    /**
     * main function for the parser.
     *
     * @param args default arguments
     */
    public static void main(final String[] args) {
        new CodeParserApp();
    }
}
