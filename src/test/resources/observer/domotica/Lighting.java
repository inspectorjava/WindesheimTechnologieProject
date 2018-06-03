/**
 * Source: https://sourcemaking.com/design_patterns/observer/java/2
 */
public class Lighting implements AlarmListener {
    public void alarm() {
        System.out.println("lights up");
    }
}
