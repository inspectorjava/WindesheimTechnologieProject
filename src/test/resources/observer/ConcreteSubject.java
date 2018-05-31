import java.util.Observable;

public class ConcreteSubject extends AbstractSubject {
    private String data;

    public ConcreteObservable () {
        data = "Test";
    }

    public String getData () {
        return data;
    }
}
