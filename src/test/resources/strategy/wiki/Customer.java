/**
 * Source: https://en.wikipedia.org/wiki/Strategy_pattern
 */

import java.util.ArrayList;
import java.util.List;

class Customer {

    private List<Double> drinks;
    private BillingStrategy strategy, strategy2;

    public Customer(final BillingStrategy strategy) {
        this.drinks = new ArrayList<Double>();
        this.strategy = strategy;
    }

    public void add(final double price, final int quantity) {
        drinks.add(strategy.getActPrice(price*quantity));
    }

    // Payment of bill
    public void printBill() {
        double sum = 0;
        for (Double i : drinks) {
            sum += i;
        }
        System.out.println("Total due: " + sum);
        drinks.clear();
    }

    // Set Strategy
    public void setStrategy(final BillingStrategy strategy) {
        this.strategy = strategy;
    }

    // Set Strategy
    public void setStrategy2(final BillingStrategy newStrategy) {
        strategy2 = newStrategy;
    }

}