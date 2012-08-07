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

import com.l7.mitra.client.ui.AccessTokenSwingClient;
import com.l7.mitra.client.ui.OAuthPropertyBean;


public class AccessPanel extends OAuthTestPanel implements
		PropertyChangeListener, DocumentListener, ActionListener {

	// Labels
	JLabel l_azToken = new JLabel("Authorization Code: ");
	JLabel l_accessToken = new JLabel("Access Token: ");
	JLabel l_refreshToken = new JLabel("Refresh Token: ");
	JLabel l_accessTokenURI = new JLabel("Token Endpoint URI: ");

	// Text Fields
	JTextField tf_azCode;
	JTextField tf_accessTokenUri;
	JTextField tf_accessToken;
	JTextField tf_refreshToken;

	// Buttons
	JButton b_refreshToken = new JButton("Refresh Token");
	JButton b_accessToken = new JButton("Retrieve Access Token");

	public AccessPanel() {
		super();

		this.panelDescription = "Request Access Token";
		this.ID = "access";
		
		OAuthPropertyBean.getInstance().addChangeListener(this);
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		setPreferredSize(new Dimension(400, 400));

		// Setup text fields
		tf_azCode = new JTextField(25);
		tf_azCode.getDocument().addDocumentListener(this);
		tf_accessToken = new JTextField(25);
		tf_accessToken.setEditable(false);
		tf_refreshToken = new JTextField(25);
		tf_refreshToken.setEditable(false);
		tf_accessTokenUri = new JTextField(25);
		tf_accessTokenUri.getDocument().addDocumentListener(this);
		tf_accessTokenUri.setText("/auth/oauth/v2/token");
		

		// Add components
		add(l_azToken);
		add(tf_azCode);
		add(l_accessTokenURI);
		add(tf_accessTokenUri);
		add(b_accessToken);
		add(l_accessToken);
		add(tf_accessToken);
		add(l_refreshToken);
		add(tf_refreshToken);

		// Add button listener
		b_accessToken.addActionListener(this);
		b_refreshToken.addActionListener(this);

		// Set Layout
		// Authorization Token
		layout.putConstraint(SpringLayout.WEST, l_azToken, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, l_azToken, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, tf_azCode, 5, SpringLayout.WEST,	this);
		layout.putConstraint(SpringLayout.NORTH, tf_azCode, 3, SpringLayout.SOUTH, l_azToken);
		
		// Token Endpoint URI
		layout.putConstraint(SpringLayout.WEST, l_accessTokenURI, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, l_accessTokenURI, 20, SpringLayout.SOUTH, tf_azCode);
		layout.putConstraint(SpringLayout.WEST, tf_accessTokenUri, 5, SpringLayout.WEST,	this);
		layout.putConstraint(SpringLayout.NORTH, tf_accessTokenUri, 3, SpringLayout.SOUTH, l_accessTokenURI);
		
		layout.putConstraint(SpringLayout.EAST, b_accessToken, 0, SpringLayout.EAST, tf_accessTokenUri);
		layout.putConstraint(SpringLayout.NORTH, b_accessToken, 20, SpringLayout.SOUTH, tf_accessTokenUri);
		
		layout.putConstraint(SpringLayout.WEST, l_accessToken, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, l_accessToken, 20, SpringLayout.SOUTH, b_accessToken);
		layout.putConstraint(SpringLayout.WEST, tf_accessToken, 5, SpringLayout.WEST,	this);
		layout.putConstraint(SpringLayout.NORTH, tf_accessToken, 3, SpringLayout.SOUTH, l_accessToken);
		
		layout.putConstraint(SpringLayout.WEST, l_refreshToken, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, l_refreshToken, 20, SpringLayout.SOUTH, tf_accessToken);
		layout.putConstraint(SpringLayout.WEST, tf_refreshToken, 5, SpringLayout.WEST,	this);
		layout.putConstraint(SpringLayout.NORTH, tf_refreshToken, 3, SpringLayout.SOUTH, l_refreshToken);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().compareTo(
				OAuthPropertyBean.AUTHORIZATION_CODE) == 0) {
			if( ((String)evt.getNewValue()).compareTo(tf_azCode.getText()) != 0)
				tf_azCode.setText(OAuthPropertyBean.getInstance()
					.getAuthorizationCode());
		}else if( evt.getPropertyName().compareTo(OAuthPropertyBean.ACCESS_TOKEN) == 0) {
			tf_accessToken.setText((String)evt.getNewValue());
		}else if( evt.getPropertyName().compareTo(OAuthPropertyBean.REFRESH_TOKEN) == 0) {
			tf_refreshToken.setText((String)evt.getNewValue());			
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if ( ae.getActionCommand().compareTo("Retrieve Access Token") == 0) {
			// TODO: Figure out what kind of grant type this is and create the appropriate access client
			AccessTokenSwingClient tokenClient = new AccessTokenSwingClient();
			// The Token URL is generated using the same server and port as the authorization server
			String tokenServerURL = "https://" + OAuthPropertyBean.getInstance().getAzHost() + ":" + 
					OAuthPropertyBean.getInstance().getAzPort() + tf_accessTokenUri.getText();			
			tokenClient.execute();
			//tokenProgressBar.setVisible(true);
			//tokenProgressBar.setIndeterminate(true);
			
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
			pBean.setAccessUri(tf_accessTokenUri.getText());
			pBean.setAuthorizationCode(tf_azCode.getText());
	}

}
