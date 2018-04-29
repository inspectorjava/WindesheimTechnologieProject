package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.FilePart;

/**
 * Created by caveman on 4/19/18.
 */
public class Singleton implements IDesignPattern {
    /**
     * The part of the file in which the singeton was found.
     */
    private FilePart filePart;

    /**
     * @return the filePart in which the singeton is found
     */
    public FilePart getFilePart() {
        return filePart;
    }

    /**
     * @param filePart the filePart in which the singeton is found
     * @return this
     */
    public Singleton setFilePart(final FilePart filePart) {
        this.filePart = filePart;
        return this;
    }
}
