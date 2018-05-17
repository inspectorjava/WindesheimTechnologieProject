package nl.windesheim.codeparser.resources.singleton;

import nl.windesheim.codeparser.patterns.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSubject {
    private String str;
    private Observer obs;
    private List<Observer> observers;

    public Subject() {
        observers = new ArrayList<>();
    }

    public void attach(Observer observer) {
        this.observers.add(observer);
    }

    public void detach(Observer observer) {
        this.observers.remove(observer);
    }

    public void signal() {
        for (Observer o : observers) {
            o.update();
        }
    }
}