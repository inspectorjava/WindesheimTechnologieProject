package nl.windesheim.codeparser;

import nl.windesheim.codeparser.marslanden.FileAnalysisProvider;
import nl.windesheim.codeparser.marslanden.patterns.IDesignPattern;

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

        FileAnalysisProvider fileAnalysisProvider = FileAnalysisProvider.getConfiguredFileAnalysisProvider();

        ClassLoader cl = this.getClass().getClassLoader();
        Path p = FileSystems.getDefault().getPath(cl.getResource("singleton").getPath());

        ArrayList<IDesignPattern> patterns = null;
        try {
            patterns = fileAnalysisProvider.analyzeDirectory(p);

            System.out.println(patterns);

        } catch (IOException e) {
            e.printStackTrace();
        }

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
