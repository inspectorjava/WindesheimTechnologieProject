/**
 * Source: https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
 */
package nl.windesheim.codeparser.resources.singleton;

class SingletonWithPublicConstructor {
    private static final SingletonWithPublicConstructor instance = new EagerInitializedSingleton();

    //private constructor to avoid client applications to use constructor
    public SingletonWithPublicConstructor(){}

    public static SingletonWithPublicConstructor getInstance(){
        return instance;
    }
}

class SingletonWithDefaultConstructor {
    private static final SingletonWithDefaultConstructor instance = new EagerInitializedSingleton();

    public static SingletonWithDefaultConstructor getInstance(){
        return instance;
    }
}

interface NotAClass {
    public unrelatedFunction(){};
}
