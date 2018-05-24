import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSubject {
    private ArrayList<MyObserver> observers, tools;

    public Subject() {
        observers = new ArrayList<>();
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