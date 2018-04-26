/**
 * Source: https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples
 */
package nl.windesheim.codeparser.resources.singleton;

public class BillPughSingleton {

    private BillPughSingleton(){}

    private static class SingletonHelper{
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance(){
        return SingletonHelper.INSTANCE;
    }
}