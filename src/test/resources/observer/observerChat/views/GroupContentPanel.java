package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFrame;

import chat.Group;
import chat.Message;
import chat.User;
import chat.UserStore;
import dialogs.AddUserDialog;
import shared.MyObserver;

public class GroupContentPanel extends JPanel implements ActionListener, MyObserver {
	private UserStore store;
	private User user;
	private Group group;
	
	private JPanel infoPanel;
	private JPanel messagesPanel;
	private JPanel submitPanel;
	private JLabel nameLabel;
	private JLabel messagesLabel;
	private JTextField submitField;
	private JButton addUserButton;
	private JButton submitButton;
	
	public GroupContentPanel (User user, UserStore store, Group group) {
		this.user = user;
		this.store = store;
		this.group = group;
		
		setLayout(new BorderLayout());
		
		// Init group information
		this.infoPanel = new JPanel();
		this.infoPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
		this.infoPanel.setLayout(new BorderLayout());
		
		this.nameLabel = new JLabel(group.getName());
		this.nameLabel.setFont(this.nameLabel.getFont().deriveFont(18.0f));
		this.infoPanel.add(this.nameLabel, BorderLayout.LINE_START);
		
		this.addUserButton = new JButton("+");
		this.addUserButton.addActionListener(this);
		this.infoPanel.add(this.addUserButton, BorderLayout.LINE_END);
		
		add(this.infoPanel, BorderLayout.PAGE_START);
		
		// Init message overview
		this.messagesPanel = new JPanel();
		this.messagesPanel.setBackground(Color.WHITE);
		this.messagesPanel.setLayout(new GridBagLayout());
		
		this.messagesLabel = new JLabel();
		this.messagesPanel.add(this.messagesLabel);
		
		JScrollPane messagesContainer = new JScrollPane(this.messagesPanel);

		add(messagesContainer, BorderLayout.CENTER);
//		add(this.messagesPanel, BorderLayout.CENTER);
		
		// Init message form
		this.submitPanel = new JPanel();
		this.submitPanel.setLayout(new BorderLayout());
		
		this.submitField = new JTextField();
		this.submitPanel.add(this.submitField, BorderLayout.CENTER);
		
		this.submitButton = new JButton("Versturen");
		this.submitButton.addActionListener(this);
		this.submitPanel.add(this.submitButton, BorderLayout.LINE_END);
		
		add(this.submitPanel, BorderLayout.PAGE_END);
		
		group.attach(this);
		
		this.update();
	}
	
	public void cleanup () {
		group.detach(this);
	}
	
	@Override
	public void update () {
		// TODO Anders vormgeven
		List<Message> messages = group.getMessages();
		
//		String toSet = "<html>";
//		
//		for (Message message : messages) {
//			toSet += message.toString() + "<br />";
//		}
//		
//		toSet += "</html>";
//		
//		this.messagesLabel.setText(toSet);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(2, 2, 2, 2);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		
		this.messagesPanel.removeAll();
		
		for (Message message : messages) {
			MessageBlock msgBlock = new MessageBlock(message);
			this.messagesPanel.add(msgBlock, c);
			c.gridy++;
		}
		
		c.weighty = 1;
		JPanel filler = new JPanel();
		filler.setPreferredSize(new Dimension(0,0));
		this.messagesPanel.add(filler, c);
		
		this.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.submitButton) {
			String messageStr = this.submitField.getText();
			if (!messageStr.isEmpty()) {
				this.user.postToGroup(this.group, messageStr);
			}
			this.submitField.setText("");
		} else if (e.getSource() == this.addUserButton) {
			AddUserDialog dialog = new AddUserDialog(this.group, this.store.getUsers());
			System.out.println("Dingen");
		}
	}
}
