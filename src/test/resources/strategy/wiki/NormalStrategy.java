/**
 * Source: https://en.wikipedia.org/wiki/Strategy_pattern
 */
// Normal billing strategy (unchanged price)
class NormalStrategy implements BillingStrategy {

    @Override
    public double getActPrice(final double rawPrice) {
        return rawPrice;
    }

}