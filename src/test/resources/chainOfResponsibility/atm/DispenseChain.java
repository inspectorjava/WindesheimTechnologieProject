/**
 * Source: https://www.journaldev.com/1617/chain-of-responsibility-design-pattern-in-java
 */
public interface DispenseChain {

    void setNextChain(DispenseChain nextChain);

    void dispense(Currency cur);
}