/**
 * class inherit.
 * type inheritance
 * Source: https://sourcemaking.com/design_patterns/observer/java/2
 */
public class Surveillance extends CheckList implements AlarmListener {
    public void alarm() {
        System.out.println("Surveillance - by the numbers:");
        byTheNumbers();
    }

    protected void isolate() {
        System.out.println("   train the cameras");
    }
}
