package chat;

import java.util.Date;

public class Message {
	private User sender;
	private String content;
	private Date sent;
	
	public Message (User sender, String content) {
		this.sender = sender;
		this.content = content;
		this.sent = new Date();
	}
	
	public User getSender () {
		return this.sender;
	}
	
	public String getContent () {
		return this.content;
	}
	
	public Date getSent () {
		return this.sent;
	}
	
	public String toString () {
		return this.sender + ": " + this.content + " (" + this.sent + ")";
	}
}
