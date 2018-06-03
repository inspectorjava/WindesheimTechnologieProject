package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chat.Group;
import chat.User;
import shared.MyObserver;

public class GroupOverviewPanel extends JPanel implements MouseListener, MyObserver {
	private ChatFrame container;
	private User user;
	private List<GroupBlock> groupPanels;
	
	public GroupOverviewPanel (ChatFrame container, User user) {
		this.container = container;
		this.user = user;
		this.groupPanels = new ArrayList<GroupBlock>();
		
		setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
		setBackground(Color.WHITE);
		setLayout(new GridBagLayout());
		
		user.attach(this);
		
		this.update();
	}
	
	public void update () {
		// TODO Niet alles in een keer leeggooien, maar lijst updaten
		for (GroupBlock gp : this.groupPanels) {
			this.remove(gp);
		}
		
		this.groupPanels.clear();
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		
		List<Group> groups = this.user.getGroups();
		for (Group group : groups) {
			GroupBlock gp = new GroupBlock(group);
			gp.addMouseListener(this);
			groupPanels.add(gp);
			this.add(gp, c);
			c.gridy++;
		}
		
		c.weighty = 1;
		JPanel filler = new JPanel();
		filler.setPreferredSize(new Dimension(0,0));
		this.add(filler, c);
		this.revalidate();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() instanceof GroupBlock) {
			GroupBlock gp = (GroupBlock) e.getSource();
			container.displayChat(gp.getGroup());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
