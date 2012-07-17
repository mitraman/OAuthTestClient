package com.l7.mitra.client.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.l7.mitra.client.oauth.CallBackServer;

public class OAuthTestClient extends  JPanel implements ActionListener, ListSelectionListener, PropertyChangeListener, MessageListener {

	//TODO: Allow the user to see the raw callback request
	String grantUrl = "";
	String accessTokenUrl = "";
	String requestUrl = "";

	// Combo Boxes
	JComboBox combo_url; 
	JComboBox combo_grantType;
	
	// List Boxes:
	JList list_steps;
	
	// Text Labels:
	JLabel l_status = new JLabel("Status Messages:");
	JLabel l_serverUrl = new JLabel("URL for Grant Request");
	
	// Text Areas:
	JTextArea ta_status = new JTextArea(8, 40);
	JTextArea ta_serverUrl = new JTextArea(2,40);
	
	// Panels
	JPanel p_inputCards;
	final static String SERVER_SETTINGS_PANEL = "serversettings";
	final static String CLIENT_SETTINGS_PANEL = "clientsettings";
	final static String AUTHORIZE_PANEL = "authorize";
	final static String ACCESS_PANEL = "access";
	final static String REQUEST_PANEL = "request";
	final static String REFRESH_PANEL = "refresh";
	JPanel p_serverSettings = new ServerSettingsPanel();
	JPanel p_clientSettings = new ClientSettingsPanel();
	JPanel p_authorize = new AuthorizationPanel();	
	JPanel p_access = new AccessPanel();
	JPanel p_refresh = new RefreshPanel();
	JPanel p_request = new RequestPanel();
	
	// TODO: Add example images to help with OAuth flow education
	JPanel p_exampleAnim = new JPanel();
	
	

	public OAuthTestClient() {
		super();
		
        // Setup message event listener for status messages
        MessageLog.getInstance().addListener(this);
        		
		// Start the call back server
		try {
			CallBackServer callBackServer = new CallBackServer(8080);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		setPreferredSize(new Dimension(650, 800));
		
		OAuthPropertyBean.getInstance().addChangeListener(this);

		// Setup the grant type chooser
		String[] grantTypes = {"Authorization Grant"};
		combo_grantType = new JComboBox(grantTypes);
		//combo_grantType.addActionListener(this);		
		
		// Set up url chooser
		String[] urlStrings = {"Grant URL", "Access Token URL", "Request URL"};
		combo_url = new JComboBox(urlStrings);
		combo_url.addActionListener(this);

		// Set up step list
		String[] stepStrings = { "1.  Authorization Server Settings", "2.  Client Application Settings", "3.  Retrieve Authorization Grant", "4.  Retrieve Access Token",
		"5.  Refresh Access Token", "6.  Send Request" };

		list_steps = new JList(stepStrings);
		list_steps.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list_steps.setLayoutOrientation(JList.VERTICAL);
		list_steps.setVisibleRowCount(8);
		list_steps.setSelectedIndex(0);		
		JScrollPane listScroller = new JScrollPane(list_steps);
		listScroller.setPreferredSize(new Dimension(250, 400));
		list_steps.addListSelectionListener(this);		

		JSeparator separator_serverResponse = new JSeparator(JSeparator.HORIZONTAL);
		separator_serverResponse.setForeground(Color.RED);
		
		// Set up server response text area
		ta_serverUrl.setEditable(true);
		ta_status.setLineWrap(true);
		JScrollPane  serverUrlScroller = new JScrollPane(ta_serverUrl);
		ta_status.setEditable(false);
		ta_status.setLineWrap(true);
		ta_status.setWrapStyleWord(true);
		//TODO: Choose a console style font
		JScrollPane serverResponseScroller = new JScrollPane(ta_status);
		serverResponseScroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			public void adjustmentValueChanged(AdjustmentEvent e) {  
			e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
			}});  
		
		
		// Setup the input panel as a CardLayout so we can switch on the fly
		p_inputCards = new JPanel(new CardLayout());
		p_inputCards.add(p_serverSettings, SERVER_SETTINGS_PANEL);
		p_inputCards.add(p_clientSettings, CLIENT_SETTINGS_PANEL);
		p_inputCards.add(p_authorize, AUTHORIZE_PANEL);
		p_inputCards.add(p_access, ACCESS_PANEL);	
		p_inputCards.add(p_refresh, REFRESH_PANEL);
		p_inputCards.add(p_request, REQUEST_PANEL);
				
		add(combo_grantType);
		add(listScroller);
		add(p_exampleAnim);
		add(separator_serverResponse);
		add(combo_url);
		add(serverUrlScroller);
		add(l_status);
		add(serverResponseScroller);
		add(p_inputCards);
		
		
		layout.putConstraint(SpringLayout.WEST, combo_grantType, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, combo_grantType, 20, SpringLayout.NORTH, this);
        
		layout.putConstraint(SpringLayout.WEST, listScroller, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, listScroller, 5, SpringLayout.SOUTH, combo_grantType);
        
        layout.putConstraint(SpringLayout.WEST, p_exampleAnim, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, p_exampleAnim, 5, SpringLayout.SOUTH, listScroller);
        
        layout.putConstraint(SpringLayout.WEST, combo_url, 14, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, combo_url, 25, SpringLayout.SOUTH, p_exampleAnim);
        layout.putConstraint(SpringLayout.WEST, serverUrlScroller, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, serverUrlScroller, -10, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, serverUrlScroller, 5, SpringLayout.SOUTH, combo_url);
        layout.putConstraint(SpringLayout.WEST, l_status, 14, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, l_status, 5, SpringLayout.SOUTH, serverUrlScroller);
        layout.putConstraint(SpringLayout.WEST, serverResponseScroller, 10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, serverResponseScroller, -10, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, serverResponseScroller, 5, SpringLayout.SOUTH, l_status);
        
        layout.putConstraint(SpringLayout.WEST, p_inputCards, 15, SpringLayout.EAST, listScroller);
        layout.putConstraint(SpringLayout.NORTH, p_inputCards, 0, SpringLayout.NORTH, listScroller);
        


	}
	
	// List Event Handlers:
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if( ae.getActionCommand().compareTo("comboBoxChanged") == 0) {
			loadUrlData();			
		}		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int index = list_steps.getSelectedIndex();
		if( index == 0 ) {
			// Load the authorization grant frame
			CardLayout cl = (CardLayout)(p_inputCards.getLayout());
	        cl.show(p_inputCards, SERVER_SETTINGS_PANEL);
	        
	        // Setup up server response area
	        //l_serverUrl.setText(p_authorize.url_label);
	        //l_serverResponse.setText(p_authorize.server_label);
	        
	        // TODO: Load last url and response from pane.
			
		}else if( index == 1 ) {
			// Load the client settings frame
			CardLayout cl = (CardLayout)(p_inputCards.getLayout());
	        cl.show(p_inputCards, CLIENT_SETTINGS_PANEL);

		}else if( index == 2) {
			CardLayout cl = (CardLayout)(p_inputCards.getLayout());
	        cl.show(p_inputCards, AUTHORIZE_PANEL);
		}else if( index == 3) {
			CardLayout cl = (CardLayout)(p_inputCards.getLayout());
	        cl.show(p_inputCards, ACCESS_PANEL);	        
		}else if( index == 4) {
			CardLayout cl = (CardLayout)(p_inputCards.getLayout());
	        cl.show(p_inputCards, REFRESH_PANEL);
		}else if( index == 5 ) {
			CardLayout cl = (CardLayout)(p_inputCards.getLayout());
	        cl.show(p_inputCards, REQUEST_PANEL);
		}
		
	}
	
	public void updateServerUrl(String url) {
		ta_serverUrl.setText(url);
	}


	
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("OAuthTestClient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        frame.getContentPane().add(new OAuthTestClient());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
 
 public static void main(String args[]) {
	//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
	 
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
            		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            		// Increase the default font to make it easier to read
            		java.util.Enumeration keys = UIManager.getDefaults().keys();
            		while (keys.hasMoreElements()) {
            	        Object key = keys.nextElement();
            	        Object value = UIManager.get (key);
            	        if( ((String)key).endsWith("font") ) {
            	        	//System.out.println(key + ":" + value);
            	        	Font oldFont = UIManager.getDefaults().getFont(key);
            	        	UIManager.put(key, oldFont.deriveFont(oldFont.getSize() + 2.0f));
            	        }
            	      } 
        		} catch (Exception e) {
        		}
                createAndShowGUI();
            }
        });
        
        
 }

@Override
public void onMessage(String msg, long timeStamp) {
	//ta_status.insert(msg + "\n", 0);
	ta_status.append(msg + "\n");
}

@Override
public void propertyChange(PropertyChangeEvent evt) {
	// Rebuild URLs after fields have been modified
	loadUrlData();
}

private void loadUrlData() {
	int urlSelection = combo_url.getSelectedIndex();
	if( urlSelection == 0) {
		ta_serverUrl.setText(OAuthPropertyBean.getInstance().getGrantURL());
	}else if( urlSelection == 1) {
		ta_serverUrl.setText(OAuthPropertyBean.getInstance().getAccessURL());
	}else if( urlSelection == 2) {
		ta_serverUrl.setText(OAuthPropertyBean.getInstance().getRequestURL());
	}
}




}
