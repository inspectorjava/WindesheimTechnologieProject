package nl.windesheim.codeparser;

import nl.windesheim.codeparser.patterns.IDesignPattern;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * App for console parser.
 */
public final class App {
    /**
     * Private constructor.
     */
    private App() {
        System.out.println("[marslanden] running");

        System.out.println("[marslanden] done");
    }

    /**
     * main function for the parser.
     *
     * @param args default arguments
     */
    public static void main(final String[] args) {
        new App();
    }
}
