package views;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import chat.Group;
import chat.User;
import chat.UserStore;

public class ChatFrame extends JFrame {
	private UserStore store;
	private User user;
	
	private GroupOverviewPanel groups;
	private GroupContentPanel content;
	
	public ChatFrame (UserStore store, User user) {
		this.store = store;
		this.user = user;
		
		setTitle("ObserverChat (" + user.getName() + ")");
		setSize(800, 600);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		groups = new GroupOverviewPanel(this, user);
		add(groups, BorderLayout.LINE_START);
		
		setVisible(true);
	}
	
	public User getUser () {
		return this.user;
	}
	
	public void displayChat (Group g) {
		if (this.content != null) {
			this.content.cleanup();
			this.remove(this.content);
		}
		
		this.content = new GroupContentPanel(this.user, this.store, g);
		this.add(this.content, BorderLayout.CENTER);
		this.revalidate();
	}
}
