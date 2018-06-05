package chat;

import java.util.ArrayList;
import java.util.List;

import shared.MyObservable;

public class Group extends MyObservable {
	private String name;
	private List<Message> messages;
	
	public Group (String name) {
		this.name = name;
		this.messages = new ArrayList<Message>();
	}
	
	public String getName () {
		return this.name;
	}
	
	public void post (Message msg) {
		this.messages.add(msg);
		this.signal();
	}
	
	public List<Message> getMessages () {
		return this.messages;
	}
	
	public Message getLastMessage () {
		return this.messages.get(this.messages.size() - 1);
	}
	
	public String toString () {
		String str = "Group: " + this.name + "\n";
		str += "------\n";
		
		for (Message msg : messages) {
			str += msg + "\n";
		}
		
		str += "\n--- End of conversation ---\n\n";
		
		return str;
	}
}
