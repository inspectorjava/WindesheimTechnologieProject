import java.util.Enumeration;
import java.util.Vector;

/**
 * Source: https://sourcemaking.com/design_patterns/observer/java/2
 */
public class SensorSystem {
    private Vector listeners = new Vector();

    public void register(AlarmListener alarmListener) {
        listeners.addElement(alarmListener);
    }

    public void soundTheAlarm() {
        for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
            ((AlarmListener) e.nextElement()).alarm();
        }
    }
}
