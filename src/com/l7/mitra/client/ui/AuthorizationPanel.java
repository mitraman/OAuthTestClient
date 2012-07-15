package com.l7.mitra.client.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class AuthorizationPanel extends JPanel implements DocumentListener, ActionListener, PropertyChangeListener {
	
	String url;
	String callBackMessage;

	
	// Text Fields
	JTextField tf_uri;
	JTextField tf_state;
	JTextField tf_redirectUri;
	JTextField tf_azCode;	
	
	// Labels
	JLabel l_uri = new JLabel("Authorization Endpoint URI: ");
	JLabel l_url = new JLabel("");
	JLabel l_redirect = new JLabel("Redirect URI: ");
	JLabel l_azCode = new JLabel("Authorization Code: ");
	JLabel l_state = new JLabel("State: ");
	
	// Buttons
	JButton b_authorize = new JButton("Authorize");
	
	public AuthorizationPanel() {

		
		super();
		
		OAuthPropertyBean.getInstance().addChangeListener(this);
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		setPreferredSize(new Dimension(400, 400));
		
		// Setup text fields
        tf_uri = new JTextField(25);
        tf_uri.setText("/auth/oauth/v2/authorize");
        tf_uri.getDocument().addDocumentListener(this);
        tf_azCode = new JTextField(25);
        tf_azCode.setEditable(false);
        tf_redirectUri = new JTextField(25);
        tf_redirectUri.setText("http://localhost:8080/callback");
        tf_redirectUri.getDocument().addDocumentListener(this);
        tf_state = new JTextField(25);
        tf_state.getDocument().addDocumentListener(this);
        tf_state.setText("OAuthTest");
        
		// Add components
        add(l_uri);
        add(tf_uri);
        add(l_redirect);
        add(tf_redirectUri);
        add(l_state);
        add(tf_state);
        add(b_authorize);
        add(l_azCode);
        add(tf_azCode);
        
        // Add button listener
        b_authorize.addActionListener(this);
        
        // Set Layout        
        //uri
        layout.putConstraint(SpringLayout.WEST, l_uri, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, l_uri, 5, SpringLayout.NORTH, this);       
        layout.putConstraint(SpringLayout.WEST, tf_uri, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, tf_uri, 3, SpringLayout.SOUTH, l_uri);
		
        
        // redirectURI
        layout.putConstraint(SpringLayout.WEST, l_redirect, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, l_redirect, 17, SpringLayout.SOUTH, tf_uri);       
        layout.putConstraint(SpringLayout.WEST, tf_redirectUri, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, tf_redirectUri, 3, SpringLayout.SOUTH, l_redirect);
        
        // state
        layout.putConstraint(SpringLayout.WEST, l_state, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, l_state, 17, SpringLayout.SOUTH, tf_redirectUri);       
        layout.putConstraint(SpringLayout.WEST, tf_state, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, tf_state, 3, SpringLayout.SOUTH, l_state);
        
        // authorize button
        layout.putConstraint(SpringLayout.EAST, b_authorize, 0, SpringLayout.EAST, tf_state);
        layout.putConstraint(SpringLayout.NORTH, b_authorize, 35, SpringLayout.SOUTH, tf_state);
        
        // read only authorization code
        layout.putConstraint(SpringLayout.WEST, l_azCode, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, l_azCode, 20, SpringLayout.SOUTH, b_authorize);       
        layout.putConstraint(SpringLayout.WEST, tf_azCode, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, tf_azCode, 3, SpringLayout.SOUTH, l_azCode);
        
        updateProperties();
	}
	
	public void loadProperties() {
		
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
		pBean.setAzUri(tf_uri.getText());
		pBean.setRedirectUri(tf_redirectUri.getText());
		pBean.setState(tf_state.getText());
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if( ae.getActionCommand().compareTo("Authorize") == 0 ) {
			java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
			 if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {
		            System.err.println( "Desktop doesn't support the browse action (fatal)" );
		            System.exit( 1 );
		     }
			 java.net.URI uri;
			try {
				uri = new java.net.URI( OAuthPropertyBean.getInstance().getGrantURL() );
				desktop.browse( uri );
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		}
	
	public void updateServerMessage(String msg) {
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if( evt.getPropertyName().compareTo(OAuthPropertyBean.AUTHORIZATION_CODE) == 0) {
			tf_azCode.setText(OAuthPropertyBean.getInstance().getAuthorizationCode());
		}
		
	}

	
}
