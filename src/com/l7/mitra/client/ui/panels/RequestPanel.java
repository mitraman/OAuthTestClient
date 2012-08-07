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
import com.l7.mitra.client.ui.ProtectedResourceSwingClient;

public class RequestPanel extends OAuthTestPanel implements
		PropertyChangeListener, DocumentListener, ActionListener {

	// Labels
	JLabel l_requestURL = new JLabel("Protected Resource URL: ");
	JLabel l_accessToken = new JLabel("Access Token: ");
	JLabel l_refreshToken = new JLabel("Refresh Token: ");

	// Text Fields
	JTextField tf_requestURL;
	JTextField tf_accessToken;
	JTextField tf_refreshToken;

	// Buttons
	JButton b_request = new JButton("Send Request");

	public RequestPanel() {
		super();

		this.panelDescription = "Send Request";
		this.ID = "request";
		
		OAuthPropertyBean.getInstance().addChangeListener(this);
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		setPreferredSize(new Dimension(400, 400));

		// Setup text fields
		tf_requestURL = new JTextField(25);
		tf_requestURL.getDocument().addDocumentListener(this);
		tf_accessToken = new JTextField(25);
		tf_accessToken.getDocument().addDocumentListener(this);
		tf_refreshToken = new JTextField(25);

		// Add components
		add(l_accessToken);
		add(tf_accessToken);
		add(l_refreshToken);
		add(tf_refreshToken);
		add(l_requestURL);
		add(tf_requestURL);
		add(b_request);

		// Add button listener
		b_request.addActionListener(this);

		// Set Layout

		layout.putConstraint(SpringLayout.WEST, l_accessToken, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, l_accessToken, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, tf_accessToken, 5, SpringLayout.WEST,	this);
		layout.putConstraint(SpringLayout.NORTH, tf_accessToken, 3, SpringLayout.SOUTH, l_accessToken);
		
		layout.putConstraint(SpringLayout.WEST, l_refreshToken, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, l_refreshToken, 20, SpringLayout.SOUTH, tf_accessToken);
		layout.putConstraint(SpringLayout.WEST, tf_refreshToken, 5, SpringLayout.WEST,	this);
		layout.putConstraint(SpringLayout.NORTH, tf_refreshToken, 3, SpringLayout.SOUTH, l_refreshToken);

		layout.putConstraint(SpringLayout.WEST, l_requestURL, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, l_requestURL, 20, SpringLayout.SOUTH, tf_refreshToken);
		layout.putConstraint(SpringLayout.WEST, tf_requestURL, 5, SpringLayout.WEST,	this);
		layout.putConstraint(SpringLayout.NORTH, tf_requestURL, 3, SpringLayout.SOUTH, l_requestURL);
		
		// Token Endpoint URI		
		layout.putConstraint(SpringLayout.EAST, b_request, 0, SpringLayout.EAST, tf_requestURL);
		layout.putConstraint(SpringLayout.NORTH, b_request, 20, SpringLayout.SOUTH, tf_requestURL);		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {	
		if( evt.getPropertyName().compareTo(OAuthPropertyBean.ACCESS_TOKEN) == 0) {
			if( ((String)evt.getNewValue()).compareTo(tf_accessToken.getText()) != 0) {
				tf_accessToken.setText((String)evt.getNewValue());
			}
		}else if( evt.getPropertyName().compareTo(OAuthPropertyBean.REFRESH_TOKEN) == 0) {
			if( ((String)evt.getNewValue()).compareTo(tf_refreshToken.getText()) != 0) {
				tf_refreshToken.setText((String)evt.getNewValue());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if ( ae.getActionCommand().compareTo("Send Request") == 0) {
			ProtectedResourceSwingClient resClient = new ProtectedResourceSwingClient();
			resClient.bearerToken = OAuthPropertyBean.getInstance().getAccessToken(); 
			resClient.url = tf_requestURL.getText(); 
			resClient.execute();
		}
		
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
			OAuthPropertyBean pBean = OAuthPropertyBean.getInstance();
			pBean.setAccessToken(tf_accessToken.getText());
			pBean.setRefreshToken(tf_refreshToken.getText());
			pBean.setRequestURL(tf_requestURL.getText());
	}

}
