/**
 * Source: https://en.wikipedia.org/wiki/Command_pattern
 */

/** The Invoker class */
public class Switch {
    private List<Command> history = new ArrayList<Command>();

    public void storeAndExecute(final Command cmd) {
        this.history.add(cmd); // optional
        cmd.execute();
    }
}