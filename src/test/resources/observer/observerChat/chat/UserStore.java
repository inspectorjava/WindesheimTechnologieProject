package chat;

import java.util.ArrayList;
import java.util.List;

public class UserStore {
	private static UserStore instance;
	
	private List<User> users;
	
	private UserStore () {
		users = new ArrayList<User>();
	}
	
	public void addUser (User user) {
		users.add(user);
	}
	
	public List<User> getUsers () {
		return this.users;
	}
	
	public static UserStore getInstance () {
		if (instance == null) {
			instance = new UserStore();
		}
		
		return instance;
	}
}
