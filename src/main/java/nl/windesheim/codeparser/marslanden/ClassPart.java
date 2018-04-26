package nl.windesheim.codeparser.marslanden;

import com.github.javaparser.Range;

import javax.swing.text.Position;
import java.io.File;

/**
 * Created by caveman on 4/19/18.
 */
public class ClassPart {
    /**
     * The file the ClassPart points to.
     */
    private File file;

    /**
     * The position in the file where the ClassPart ends at.
     */
    private Range range;

    /**
     * Returns the file of the ClassPart.
     * @return File
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the file of the ClassPart.
     * @param file File
     * @return File
     */
    public ClassPart setFile(final File file) {
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
    public ClassPart setRange(Range range) {
        this.range = range;
        return this;
    }
}
