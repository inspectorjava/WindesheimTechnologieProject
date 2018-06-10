package shared;

import java.util.ArrayList;
import java.util.List;

public abstract class MyObservable {
	private List<MyObserver> obs = new ArrayList<MyObserver>();
	
	public void attach (MyObserver o) {
		this.obs.add(o);
	}
	
	public void detach (MyObserver o) {
		this.obs.remove(o);
	}
	
	public void signal () {
		for (MyObserver o : this.obs) {
			o.update();
		}
	}
}
