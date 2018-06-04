/**
 * Source: https://en.wikipedia.org/wiki/Command_pattern
 */

/**
 * The Command functional interface.
 */
@FunctionalInterface
public interface Command {
    public void apply();
}
