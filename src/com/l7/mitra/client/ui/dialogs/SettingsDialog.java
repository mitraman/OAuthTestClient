package com.l7.mitra.client.ui.dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.l7.mitra.client.ui.OAuthPropertyBean;

public class SettingsDialog extends JDialog implements ActionListener {

	JLabel l_serverPort = new JLabel("Callback Server Port: ");
	
	JTextField tf_serverPort = new JTextField(4);
	
	JButton b_OK = new JButton("OK");
	JButton b_Cancel = new JButton("Cancel");
	
	public SettingsDialog(Frame aFrame) {
		super(aFrame, true);
		
		setTitle("Settings");
		setPreferredSize(new Dimension(200,200));
		setSize(new Dimension(300,200));
		
		b_OK.setMargin(new Insets(1, 25, 1, 25));
		b_OK.addActionListener(this);
		b_Cancel.setMargin(new Insets(1, 25, 1, 25));
		b_Cancel.addActionListener(this);
		
		tf_serverPort.setText(OAuthPropertyBean.getInstance().getServerPort());
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		
		JPanel settingsPane = new JPanel();
		SpringLayout settingsLayout = new SpringLayout();
		settingsPane.setLayout(settingsLayout);
		settingsPane.add(l_serverPort);
		settingsPane.add(tf_serverPort);
		
		settingsLayout.putConstraint(SpringLayout.WEST, l_serverPort, 20, SpringLayout.WEST, settingsPane);
		settingsLayout.putConstraint(SpringLayout.NORTH, l_serverPort, 20, SpringLayout.NORTH, settingsPane);
		settingsLayout.putConstraint(SpringLayout.WEST, tf_serverPort, 10, SpringLayout.EAST, l_serverPort);
		settingsLayout.putConstraint(SpringLayout.NORTH, tf_serverPort, 20, SpringLayout.NORTH, settingsPane);
		
		//Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(b_OK);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(b_Cancel);

		add(settingsPane);
		add(buttonPane);
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if( ae.getActionCommand().compareTo("OK") == 0 ) {
			// Save settings to property bean and close dialog
			OAuthPropertyBean.getInstance().setServerPort(tf_serverPort.getText());
			dispose();
		}else if( ae.getActionCommand().compareTo("Cancel") == 0) {
			dispose();
		}
		
	}

}
