package com.l7.mitra.client.ui;

import java.util.ArrayList;
import java.util.Iterator;

public class MessageLog {
	
	private static MessageLog _instance;
	private ArrayList messageListeners = new ArrayList();
	
	private MessageLog() {
		
	}
	
	public static MessageLog getInstance() {
		if( _instance == null) {
			_instance = new MessageLog();
		}
		return _instance;
	}
	
	public void addMessage(String msg) {
		Iterator it = messageListeners.iterator();
		long timestamp = System.currentTimeMillis();
		while( it.hasNext()) {
			MessageListener listener = (MessageListener)it.next();
			listener.onMessage(msg, timestamp);
		}
	}
	
	public void addListener(MessageListener listener) {
		messageListeners.add(listener);
	}

}
