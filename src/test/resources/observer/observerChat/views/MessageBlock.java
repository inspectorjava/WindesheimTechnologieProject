package views;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import chat.Message;

public class MessageBlock extends JPanel {
	public MessageBlock (Message message) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setSize(300, 20);
		
		JLabel nameLabel = new JLabel(message.getSender().getName());
		nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(nameLabel);
		
		JLabel messageLabel = new JLabel(message.getContent());
		messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(messageLabel);
		
		JLabel timestampLabel = new JLabel(message.getSent().toString());
		timestampLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(timestampLabel);
	}
}
