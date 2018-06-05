package chat;

import java.util.ArrayList;
import java.util.List;

import shared.MyObservable;

public class User extends MyObservable {
	private String name;
	private List<Group> groups;
	
	public User (String name) {
		this.name = name;
		this.groups = new ArrayList<Group>();
	}
	
	public String getName () {
		return this.name;
	}
	
	public List<Group> getGroups () {
		return this.groups;
	}
	
	public void joinGroup (Group group) {
		if (!this.groups.contains(group)) {
			this.groups.add(group);
			this.signal();
		}
	}
	
	public void leaveGroup (Group group) {
		if (!this.groups.contains(group)) {
			this.groups.add(group);
			this.signal();
		}
	}
	
	public void postToGroup (Group group, String content) {
		if (groups.contains(group)) {
			Message msg = new Message(this, content);
			group.post(msg);
		}
	}
	
	public String toString () {
		return this.name;
	}
}
