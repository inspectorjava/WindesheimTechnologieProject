/**
 * Source: https://en.wikipedia.org/wiki/Strategy_pattern
 */
// Strategy for Happy hour (50% discount)
class HappyHourStrategy implements BillingStrategy {

    @Override
    public double getActPrice(final double rawPrice) {
        return rawPrice*0.5;
    }

}