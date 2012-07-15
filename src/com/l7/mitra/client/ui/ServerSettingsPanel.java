package com.l7.mitra.client.ui;

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

public class ServerSettingsPanel extends JPanel implements
		PropertyChangeListener, DocumentListener, ActionListener {

	// Labels
	JLabel l_hostname = new JLabel("Hostname: ");
	JLabel l_port = new JLabel("Port: ");

	// Text Fields
	JTextField tf_host;
	JTextField tf_port;

	public ServerSettingsPanel() {
		super();

		OAuthPropertyBean.getInstance().addChangeListener(this);
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		setPreferredSize(new Dimension(400, 400));

		// Setup text fields
		tf_host = new JTextField(16);
		tf_host.getDocument().addDocumentListener(this);
		tf_port = new JTextField(4);
		tf_port.getDocument().addDocumentListener(this);
		tf_port.setText("8443");

		// Add components
		add(l_hostname);
		add(tf_host);
		add(l_port);
		add(tf_port);

		// Set Layout
        // hostname
        layout.putConstraint(SpringLayout.WEST, l_hostname, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, l_hostname, 5, SpringLayout.NORTH, this);       
        layout.putConstraint(SpringLayout.WEST, tf_host, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, tf_host, 3, SpringLayout.SOUTH, l_hostname);
        
        //port
        layout.putConstraint(SpringLayout.WEST, tf_port, 10, SpringLayout.EAST, tf_host);
        layout.putConstraint(SpringLayout.NORTH, tf_port, 0, SpringLayout.NORTH, tf_host);
        layout.putConstraint(SpringLayout.WEST, l_port, 0, SpringLayout.WEST, tf_port);
        layout.putConstraint(SpringLayout.SOUTH, l_port, -3, SpringLayout.NORTH, tf_port);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
//		if (evt.getPropertyName().compareTo(
//				OAuthPropertyBean.AZ_HOST) == 0) {
//			tf_host.setText(OAuthPropertyBean.getInstance()
//					.getAzHost());
//		}else if (evt.getPropertyName().compareTo(
//				OAuthPropertyBean.AZ_PORT) == 0) {
//			tf_port.setText(OAuthPropertyBean.getInstance()
//					.getAzPort());
//		}

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
		prop.setAzHost(tf_host.getText());
		prop.setAzPort(tf_port.getText());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
