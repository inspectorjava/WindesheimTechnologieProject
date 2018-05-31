package observer;

import java.util.Observable;

public class ConcreteObservable extends Observable {
    private String data;

    public ConcreteObservable () {
        data = "Test";
    }

    public String getData () {
        return data;
    }
}
