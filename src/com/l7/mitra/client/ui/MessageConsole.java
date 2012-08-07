package com.l7.mitra.client.ui;

import javax.swing.JTextArea;
import javax.swing.text.Document;

public class MessageConsole extends JTextArea {

	int maxLineCount = 5000;
	
	public MessageConsole() {
		// TODO Auto-generated constructor stub
	}
	
	public MessageConsole(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public MessageConsole(Document arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public MessageConsole(int arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public MessageConsole(String arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public MessageConsole(Document arg0, String arg1, int arg2, int arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void append(String str) {
		// TODO Auto-generated method stub
		super.append(str);
		
		// Make sure we stay under our line limit
		if( this.getLineCount() > maxLineCount ) {
			int deltaCount = getLineCount() - maxLineCount;
			
		}
	}

	@Override
	public void insert(String str, int pos) {
		// TODO Auto-generated method stub
		//super.insert(str, pos);
		// We won't allow insertion of lines.
		return;
	}
	
	

}
