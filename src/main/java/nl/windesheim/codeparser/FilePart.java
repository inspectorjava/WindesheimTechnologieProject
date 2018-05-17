package nl.windesheim.codeparser;

import com.github.javaparser.Range;

import java.io.File;

/**
 * Created by caveman on 4/19/18.
 */
public class FilePart {
    /**
     * The file the FilePart points to.
     */
    private File file;

    /**
     * The position in the file where the FilePart ends at.
     */
    private Range range;

    /**
     * Returns the file of the FilePart.
     *
     * @return File
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the file of the FilePart.
     *
     * @param file File
     * @return File
     */
    public FilePart setFile(final File file) {
        this.file = file;
        return this;
    }

    /**
     * @return character range in file
     */
    public Range getRange() {
        return range;
    }

    /**
     * @param range character range in file
     * @return instance of this
     */
    public FilePart setRange(final Range range) {
        this.range = range;
        return this;
    }

    @Override
    public String toString() {
        return "FilePart{"
                + "file=" + file
                + ", range=" + range
                + '}';
    }
}
