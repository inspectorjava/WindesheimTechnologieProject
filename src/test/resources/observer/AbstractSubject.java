import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractSubject {
    private ArrayList<MyObserver> observers, tools;
    private Set<MyObserver> observerSet;

    public Subject() {
        observers = new ArrayList<>();
        observerSet = new HashSet<>();
    }

    public void attach(MyObserver observer) {
        this.observers.add(observer);
    }

    public void detach(MyObserver observer) {
        this.observers.remove(observer);
    }

    public void signal() {
        for (MyObserver o : observers) {
            o.update();
        }
    }
}