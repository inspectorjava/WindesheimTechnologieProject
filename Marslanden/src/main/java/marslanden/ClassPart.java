package marslanden;

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
     * The position in the file where the ClassPart starts at.
     */
    private Position startPosition;

    /**
     * The position in the file where the ClassPart ends at.
     */
    private Position endPosition;

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
     * @return Returns the start position of the class part.
     */
    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * Sets the start position of the class part.
     * @param startPosition the start position
     * @return returns this object
     */
    public ClassPart setStartPosition(final Position startPosition) {
        this.startPosition = startPosition;
        return this;
    }

    /**
     * @return Returns the end position of the class part
     */
    public Position getEndPosition() {
        return endPosition;
    }

    /**
     * Sets the end position of the class part.
     * @param endPosition the end position
     * @return returns this object
     */
    public ClassPart setEndPosition(final Position endPosition) {
        this.endPosition = endPosition;
        return this;
    }
}
