/**
 * Source: https://sourcemaking.com/design_patterns/observer/java/2
 */
public class Gates implements AlarmListener {
    public void alarm() {
        System.out.println("gates close");
    }
}
