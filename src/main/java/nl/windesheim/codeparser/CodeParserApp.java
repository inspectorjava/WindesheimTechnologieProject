package nl.windesheim.codeparser;

import nl.windesheim.codeparser.patterns.IDesignPattern;
import nl.windesheim.codeparser.patterns.Observer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
//            Path path = Paths.get("/Users/rickbos/WindesheimTechnologieProject/src/test/resources/observer/");
            Path path = Paths.get("/Users/rickbos/WindesheimTechnologieProject/src/test/resources/strategy/");
//            Path path = Paths.get("/Users/rickbos/WindesheimTechnologieProject/src/test/resources/singleton/");
            List<IDesignPattern> patterns = analysis.analyzeDirectory(path);

            for (IDesignPattern p : patterns) {
                Observer o = (Observer) p;
                System.out.println("Found " + o.toString());

//                Singleton s = (Singleton) p;
//                System.out.println("Found" + s.toString() + " in file " + s.getClassPart().getFile().getName());
            }
        } catch (IOException ex) {
            System.out.println("Krak: " + ex.getMessage());
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
