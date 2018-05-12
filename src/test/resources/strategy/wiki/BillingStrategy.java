/**
 * Source: https://en.wikipedia.org/wiki/Strategy_pattern
 */
interface BillingStrategy {
    double getActPrice(final double rawPrice);
}