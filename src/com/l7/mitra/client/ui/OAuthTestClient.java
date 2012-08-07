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
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.l7.mitra.client.oauth.CallBackServer;
import com.l7.mitra.client.oauth.CallBackServerManager;
import com.l7.mitra.client.ui.dialogs.SettingsDialog;
import com.l7.mitra.client.ui.panels.AccessPanel;
import com.l7.mitra.client.ui.panels.AuthorizationPanel;
import com.l7.mitra.client.ui.panels.ClientSettingsPanel;
import com.l7.mitra.client.ui.panels.OAuthTestPanel;
import com.l7.mitra.client.ui.panels.RefreshPanel;
import com.l7.mitra.client.ui.panels.RequestPanel;
import com.l7.mitra.client.ui.panels.ServerSettingsPanel;

public class OAuthTestClient extends  JPanel implements ActionListener, ListSelectionListener, PropertyChangeListener, MessageListener {

	JFrame applicationFrame;
	
	
	private static final String[] grantTypes = {"Authorization Code Grant", "Resource Owner Password Credentials Grant", "Client Credentials Grant"};
	private static final int AUTHORIZATION_GRANT_INDEX = 0;
	private static final int IMPLICIT_GRANT_INDEX = 5;
	private static final int PASSWORD_GRANT_INDEX = 1;
	private static final int CLIENT_GRANT_INDEX = 2;
	
	
	
	
	//TODO: Allow the user to see the raw callback request
	String grantUrl = "";
	String accessTokenUrl = "";
	String requestUrl = "";

	// Combo Boxes
	JComboBox combo_url; 
	JComboBox combo_grantType;
	
	// List Boxes:
	JList list_steps;
	GrantStepsListModel listmodel_steps = new GrantStepsListModel();
	
	// Text Labels:
	JLabel l_status = new JLabel("Status Messages:");
	JLabel l_serverUrl = new JLabel("URL for Grant Request");
	
	// Text Areas:
	JTextArea ta_status = new JTextArea(8, 40);
	JTextArea ta_serverUrl = new JTextArea(2,40);
	
	// Panels
	JPanel p_inputCards;
	
	// TODO: Add example images to help with OAuth flow education
	JPanel p_exampleAnim = new JPanel();

	
	// Top Menu
	JMenuBar menuBar = new JMenuBar();
	
	// Static strings
	final static String SETTINGS_MENUITEM = "Edit Settings...";
	final static String LOAD_CONFIG_MENUITEM = "Load Properties...";
	final static String SAVE_CONFIG_MENUITEM = "Save Properties...";		
	final static String REST_CONFIG_MENUITEM = "Reset Properties";
	final static String FILE_MENU = "File";
	final static String HELP_MENU = "Help";
	final static String ABOUT_MENUITEM = "About";

	public OAuthTestClient(JFrame applicationFrame) {
		super();
		
		this.applicationFrame = applicationFrame;
		
        // Setup message event listener for status messages
        MessageLog.getInstance().addListener(this);
        		
		// Start the call back server
		CallBackServerManager callBackServerManager = new CallBackServerManager();
		// TODO: the starting port should be read from a config file.
		OAuthPropertyBean.getInstance().setServerPort("8080");
		

		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		setPreferredSize(new Dimension(650, 800));
		
		OAuthPropertyBean.getInstance().addChangeListener(this);
		
		// Setup the top menubar
		JMenu fileMenu = new JMenu(FILE_MENU);	
		fileMenu.setMnemonic('F');
		JMenuItem saveMenuItem = new JMenuItem(SAVE_CONFIG_MENUITEM, KeyEvent.VK_S);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveMenuItem.addActionListener(this);
		saveMenuItem.setMnemonic('S');
		fileMenu.add(saveMenuItem);
		JMenuItem loadMenuItem = new JMenuItem(LOAD_CONFIG_MENUITEM, KeyEvent.VK_L);
		loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		loadMenuItem.addActionListener(this);
		fileMenu.add(loadMenuItem);
		JMenuItem settingsMenuItem = new JMenuItem(SETTINGS_MENUITEM, KeyEvent.VK_E);
		settingsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
		settingsMenuItem.addActionListener(this);
		fileMenu.add(settingsMenuItem);
		menuBar.add(fileMenu);
		
		JMenu helpMenu = new JMenu(HELP_MENU);
		JMenuItem aboutMenuItem = new JMenuItem(ABOUT_MENUITEM);
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		
		applicationFrame.setJMenuBar(menuBar);
		

		// Setup the grant type chooser
		
		combo_grantType = new JComboBox(grantTypes);
		combo_grantType.addActionListener(this);		
		
		// Set up url chooser
		String[] urlStrings = {"Grant URL", "Access Token URL", "Request URL"};
		combo_url = new JComboBox(urlStrings);
		combo_url.addActionListener(this);
		
		list_steps = new JList(listmodel_steps);
		list_steps.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list_steps.setLayoutOrientation(JList.VERTICAL);
		list_steps.setVisibleRowCount(8);
		list_steps.setSelectedIndex(0);
		OAuthPropertyBean.getInstance().setGrantType(OAuthPropertyBean.AUTHORIZATION_GRANT);
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
		setupPanels();
						
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
        layout.putConstraint(SpringLayout.NORTH, listScroller, 15, SpringLayout.SOUTH, combo_grantType);
        
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
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if( ae.getActionCommand().compareTo("comboBoxChanged") == 0) {				
			if( ae.getSource() == combo_url) {
				// The user has selected a URL from the URL dropdown viewer
				loadUrlData();
			}else if( ae.getSource() == combo_grantType) {
				// The OAuth grant type has been changed.
				if( combo_grantType.getSelectedIndex() == AUTHORIZATION_GRANT_INDEX) {
					OAuthPropertyBean.getInstance().setGrantType(OAuthPropertyBean.AUTHORIZATION_GRANT);
					setupPanels();										
				}else if(combo_grantType.getSelectedIndex() == PASSWORD_GRANT_INDEX) {
					OAuthPropertyBean.getInstance().setGrantType(OAuthPropertyBean.PASWORD_GRANT);
					setupPanels();
				}else if( combo_grantType.getSelectedIndex() == CLIENT_GRANT_INDEX) {
					OAuthPropertyBean.getInstance().setGrantType(OAuthPropertyBean.CLIENT_CREDENTIALS_GRANT);
					setupPanels();
				}

			}
		}else if( ae.getActionCommand().compareTo(ABOUT_MENUITEM) == 0) {
			JOptionPane.showMessageDialog(applicationFrame,
				    "OAuthTestClient\nAn OAuth 2.0 Test Application\nWritten by Ronnie Mitra.\n\nhttps://github.com/mitraman/OAuthTestClient    \n\n",
				    "About OAuthTestClient",
				    JOptionPane.QUESTION_MESSAGE);
		}else if( ae.getActionCommand().compareTo(SETTINGS_MENUITEM) == 0) {
			SettingsDialog settingsDialog = new SettingsDialog(applicationFrame);
			settingsDialog.setVisible(true);
		}else if( ae.getActionCommand().compareTo(SAVE_CONFIG_MENUITEM) == 0) {
			// TODO: set a default file name
			JFileChooser fileChooser = new JFileChooser("./OAuthTestClient.cfg");
			if( fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ) {
				File file = fileChooser.getSelectedFile();
				ConfigManager.saveProperties(file);
			}
			
		}else if( ae.getActionCommand().compareTo(LOAD_CONFIG_MENUITEM) == 0 ) {
			JFileChooser fileChooser = new JFileChooser();			
			if( fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				ConfigManager.loadProperties(file);
			}
		}
		else {
			System.out.println(ae.getActionCommand() + "," + ae.getID());
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int index = list_steps.getSelectedIndex();		
		CardLayout cl = (CardLayout)(p_inputCards.getLayout());
        cl.show(p_inputCards, listmodel_steps.getPanel(index).ID);
	}
	
	public void updateServerUrl(String url) {
		ta_serverUrl.setText(url);
	}

	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("OAuthTestClient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        frame.getContentPane().add(new OAuthTestClient(frame));
 
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

// utility method to setup card object with panels depending on list model
private void setupPanels() {
	p_inputCards.removeAll();					
	for( int i = 0; i < listmodel_steps.getSize(); i++ ) {
		OAuthTestPanel panel = listmodel_steps.getPanel(i);
		p_inputCards.add(panel, panel.ID);
	}
}




}
