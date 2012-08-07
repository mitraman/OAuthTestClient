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


public class ImplicitGrantPanel extends OAuthTestPanel implements
		PropertyChangeListener, DocumentListener, ActionListener {
		
	// Labels
	JLabel l_javascript = new JLabel("JavaScript Code: ");

	// Text Fields
	JTextField tf_clientId;
	JTextField tf_clientSecret;
	
	// Buttons
	JButton b_Launch = new JButton("Launch Web Client...");

	public ImplicitGrantPanel() {
		super();

		this.panelDescription = "Client Settings";
		this.ID = "clientsettings";
		
		OAuthPropertyBean.getInstance().addChangeListener(this);
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		setPreferredSize(new Dimension(400, 400));

		// Setup text fields
		tf_clientId = new JTextField(25);
		tf_clientId.getDocument().addDocumentListener(this);
		tf_clientSecret = new JTextField(25);
		tf_clientSecret.getDocument().addDocumentListener(this);

		// Add components
		add(l_javascript);
		add(tf_clientId);

		// Set Layout
		layout.putConstraint(SpringLayout.WEST, l_javascript, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, l_javascript, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, tf_clientId, 5, SpringLayout.WEST,	this);
		layout.putConstraint(SpringLayout.NORTH, tf_clientId, 3, SpringLayout.SOUTH, l_javascript);
		
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if( evt.getPropertyName().compareTo(OAuthPropertyBean.CLIENT_ID) == 0) {
			if( ((String)evt.getNewValue()).compareTo(tf_clientId.getText()) != 0) {
				tf_clientId.setText((String)evt.getNewValue());
			}
		}else if( evt.getPropertyName().compareTo(OAuthPropertyBean.CLIENT_SECRET) == 0) {
			if( ((String)evt.getNewValue()).compareTo(tf_clientSecret.getText()) != 0) {
				tf_clientSecret.setText((String)evt.getNewValue());
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
		prop.setClientId(tf_clientId.getText());
		prop.setClientSecret(tf_clientSecret.getText());
	}

}
