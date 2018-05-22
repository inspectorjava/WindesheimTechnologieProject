/**
 * Source: https://www.journaldev.com/1617/chain-of-responsibility-design-pattern-in-java
 */
public class Currency {

    private int amount;

    public Currency(int amt){
        this.amount=amt;
    }

    public int getAmount(){
        return this.amount;
    }
}