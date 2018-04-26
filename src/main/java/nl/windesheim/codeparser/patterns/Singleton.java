package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassPart;

/**
 * Created by caveman on 4/19/18.
 */
public class Singleton implements IDesignPattern {
    /**
     * The part of the file in which the singeton was found.
     */
    private ClassPart classPart;

    /**
     * @return the classPart in which the singeton is found
     */
    public ClassPart getClassPart() {
        return classPart;
    }

    /**
     * @param classPart the classPart in which the singeton is found
     * @return this
     */
    public Singleton setClassPart(final ClassPart classPart) {
        this.classPart = classPart;
        return this;
    }
}
