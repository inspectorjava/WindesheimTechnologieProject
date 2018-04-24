package marslanden;

import marslanden.patterns.IDesignPattern;
import parser.ParserWrapper;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Main class for the marslanden project.
 */
public final class Main {

    /**
     * Private constructor.
     */
    private Main() {
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
     * main function.
     * @param args default arguments
     */
    public static void main(final String[] args) {
        new Main();
    }

}
