/**
 * Source: https://sourcemaking.com/design_patterns/command/java/1
 */

class Politician implements Command {
    public void execute() {
        System.out.println("take money from the rich, take votes from the poor");
    }
}