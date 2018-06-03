package dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chat.Group;
import chat.User;

public class AddUserDialog extends JDialog implements ActionListener {
	private Group group;
	
	private JComboBox<User> userSelection;
	private JButton closeButton;
	private JButton addButton;
	
	public AddUserDialog (Group group, List<User> users) {
		this.group = group;
		
		setSize(400, 300);
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		c.gridy = 0;
		
		// Title
		JLabel titleLabel = new JLabel("Toevoegen aan groep");
		this.add(titleLabel, c);
		
		// User selection
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.LINE_AXIS));
		
		User[] userArr = new User[users.size()];
		users.toArray(userArr);
		this.userSelection = new JComboBox<User>(userArr);
		selectionPanel.add(this.userSelection);
		
		this.addButton = new JButton("+");
		this.addButton.addActionListener(this);
		selectionPanel.add(this.addButton);
		
		c.gridy++;
		this.add(selectionPanel, c);
		
		// Buttons
		this.closeButton = new JButton("Sluiten");
		this.closeButton.addActionListener(this);
		c.gridy++;
		this.add(closeButton, c);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.addButton) {
			User toAdd = (User) userSelection.getSelectedItem();
			toAdd.joinGroup(this.group);
		} else if (e.getSource() == this.closeButton) {
			// Sluit modal
			dispose();
		}
	}
}
