import chat.User;
import chat.UserStore;
import views.ChatFrame;
import chat.Group;

public class ObserverChat {
	public static void main(String[] args) {
		User u1 = new User ("Henk");
		User u2 = new User ("Rick");
		User u3 = new User ("Pieter");
		User u4 = new User ("Van Poeidensteyn");
		
		Group g1 = new Group("De Poeides");
		Group g2 = new Group("Iedereen");
		
		u2.joinGroup(g1);
		u4.joinGroup(g1);
		
		u1.joinGroup(g2);
		u2.joinGroup(g2);
		u3.joinGroup(g2);
		u4.joinGroup(g2);
		
		u1.postToGroup(g2, "Hallo, dit is een testbericht");
		u2.postToGroup(g2, "Dit is nog een testbericht");
		u1.postToGroup(g2, "Leuk he?");
		u4.postToGroup(g2, "Dit is wel cool ja");
		
		UserStore store = UserStore.getInstance();
		store.addUser(u1);
		store.addUser(u2);
		store.addUser(u3);
		store.addUser(u4);
		
//		System.out.println(g2);
		
		ChatFrame frame1 = new ChatFrame(store, u1);
		ChatFrame frame2 = new ChatFrame(store, u2);
	}
}
