package com.l7.mitra.client.ui.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.l7.mitra.client.ui.OAuthPropertyBean;


public class ResourceOwnerSettingsPanel extends OAuthTestPanel implements
		PropertyChangeListener, DocumentListener, ActionListener {
	
	// Labels
	JLabel l_username = new JLabel("Username: ");
	JLabel l_password = new JLabel("Password: ");

	// Text Fields
	JTextField tf_username;
	JTextField tf_password;

	public ResourceOwnerSettingsPanel() {
		super();

		this.panelDescription = "Resource Owner Settings";
		this.ID = "resourceowner";
		
		OAuthPropertyBean.getInstance().addChangeListener(this);
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		setPreferredSize(new Dimension(400, 400));

		// Setup text fields
		tf_username = new JTextField(25);
		tf_username.getDocument().addDocumentListener(this);
		tf_password = new JTextField(25);
		tf_password.getDocument().addDocumentListener(this);

		// Add components
		add(l_username);
		add(tf_username);
		add(l_password);
		add(tf_password);

		// Set Layout
		layout.putConstraint(SpringLayout.WEST, l_username, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, l_username, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, tf_username, 5, SpringLayout.WEST,	this);
		layout.putConstraint(SpringLayout.NORTH, tf_username, 3, SpringLayout.SOUTH, l_username);
		
		layout.putConstraint(SpringLayout.WEST, l_password, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, l_password, 20, SpringLayout.SOUTH, tf_username);
		layout.putConstraint(SpringLayout.WEST, tf_password, 5, SpringLayout.WEST,	this);
		layout.putConstraint(SpringLayout.NORTH, tf_password, 3, SpringLayout.SOUTH, l_password);		
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if( evt.getPropertyName().compareTo(OAuthPropertyBean.RES_USERNAME) == 0) {
			if( ((String)evt.getNewValue()).compareTo(tf_username.getText()) != 0) {
				tf_username.setText((String)evt.getNewValue());
			}
		}else if( evt.getPropertyName().compareTo(OAuthPropertyBean.RES_PASSWORD) == 0) {
			if( ((String)evt.getNewValue()).compareTo(tf_password.getText()) != 0) {
				tf_password.setText((String)evt.getNewValue());
			}
		}
//		

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		updateProperties();
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		updateProperties();
		
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		updateProperties();
		
	}
	
	private void updateProperties() {
		// Doing this the lazy way until I figure out exactly which field has changed.
		OAuthPropertyBean prop = OAuthPropertyBean.getInstance();
		prop.setResUsername(tf_username.getText());
		prop.setResPassword(tf_password.getText());
	}

}
