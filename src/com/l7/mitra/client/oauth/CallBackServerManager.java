package com.l7.mitra.client.oauth;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import com.l7.mitra.client.ui.MessageLog;
import com.l7.mitra.client.ui.OAuthPropertyBean;

public class CallBackServerManager implements PropertyChangeListener {

	CallBackServer callBackServer;
	
	public CallBackServerManager() {
		OAuthPropertyBean.getInstance().addChangeListener(this);
	}
	

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if( evt.getPropertyName().compareTo(OAuthPropertyBean.SERVER_PORT) == 0) {
			// Start the server with the new server port.
			
			if( callBackServer != null) {
				callBackServer.stop();	
			}
			
			try {
				callBackServer = new CallBackServer(Integer.parseInt((String)evt.getNewValue()));
			} catch (NumberFormatException e) {
				MessageLog.getInstance().addMessage("Unable to start callback listener - " + evt.getNewValue() + " is not a valid port number.");
			} catch (IOException e) {
				MessageLog.getInstance().addMessage(e.getMessage());
			}
		}
		
	}

	
	

}
