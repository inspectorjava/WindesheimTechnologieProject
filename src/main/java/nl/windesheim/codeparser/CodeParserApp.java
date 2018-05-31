package nl.windesheim.codeparser;

import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.ObserverPattern;

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

        try {
            FileAnalysisProvider analysis = FileAnalysisProvider.getConfiguredFileAnalysisProvider();
            ClassLoader classLoader = this.getClass().getClassLoader();
            String pathString = "/Users/rickbos/WindesheimTechnologieProject/src/test/resources/observer";
            File codeDir = new File(pathString);

            List<IDesignPattern> patterns = analysis.analyzeDirectory(codeDir.toPath());

            for (IDesignPattern p : patterns) {
                ObserverPattern o = (ObserverPattern) p;
                System.out.println("Found " + o.toString());
            }
        } catch (IOException ex) {
            System.out.println("Krak IOException: " + ex.getMessage());
        }

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
