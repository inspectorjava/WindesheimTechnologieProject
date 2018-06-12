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
     * True if the singleton has a private constructor.
     */
    private boolean privateConstr;

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

    /**
     * @return true/false
     */
    public boolean hasPrivateConstructor() {
        return privateConstr;
    }

    /**
     * @param privateConstr true/false
     * @return this
     */
    public Singleton setPrivateConstructor(final boolean privateConstr) {
        this.privateConstr = privateConstr;
        return this;
    }
}
