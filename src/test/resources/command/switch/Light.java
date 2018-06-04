/**
 * Source: https://en.wikipedia.org/wiki/Command_pattern
 */

/** The Receiver class */
public class Light {

    public void turnOn() {
        System.out.println("The light is on");
    }

    public void turnOff() {
        System.out.println("The light is off");
    }
}