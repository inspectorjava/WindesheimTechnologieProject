package nl.windesheim.codeparser.patterns;

import nl.windesheim.codeparser.ClassOrInterface;

/**
 * Created by caveman on 4/19/18.
 */
public class Singleton implements IDesignPattern {
    /**
     * The part of the file in which the singeton was found.
     */
    private ClassOrInterface singletonClass;

    /**
     * @return the singletonClass in which the singeton is found
     */
    public ClassOrInterface getSingletonClass() {
        return singletonClass;
    }

    /**
     * @param singletonClass the singletonClass in which the singeton is found
     * @return this
     */
    public Singleton setSingletonClass(final ClassOrInterface singletonClass) {
        this.singletonClass = singletonClass;
        return this;
    }
}
