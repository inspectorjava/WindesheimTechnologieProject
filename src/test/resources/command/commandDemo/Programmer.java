/**
 * Source: https://sourcemaking.com/design_patterns/command/java/1
 */

class Programmer implements Command {
    public void execute() {
        System.out.println("sell the bugs, charge extra for the fixes");
    }
}
