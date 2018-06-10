package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chat.Group;
import shared.MyObserver;

public class GroupBlock extends JPanel implements MyObserver {
	private Group group;
	
	private JLabel infoLabel;
	
	public GroupBlock (Group group) {
		this.group = group;
		
		int messageCnt = group.getMessages().size();
		this.infoLabel = new JLabel(group.getName() + " (" + String.valueOf(messageCnt) + ")");
		
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		setBackground(Color.WHITE);
		setLayout(new FlowLayout());
		this.setPreferredSize(new Dimension(200, 50));
		
		group.attach(this);
		
		add(infoLabel);
	}
	
	public Group getGroup () {
		return this.group;
	}
	
	public void cleanup () {
		group.detach(this);
	}

	@Override
	public void update() {
		int messageCnt = group.getMessages().size();
		this.infoLabel.setText(group.getName() + " (" + String.valueOf(messageCnt) + ")");
	}
}
