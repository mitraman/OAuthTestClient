package com.l7.mitra.client.ui;

import java.io.IOException;

import javax.swing.SwingWorker;

import com.l7.mitra.client.oauth.ProtectedResourceClient;

public class ProtectedResourceSwingClient extends SwingWorker {

	public String url = "";
	public String bearerToken = "";
	

	ProtectedResourceSwingClient() {
	}
	
	@Override
	protected Object doInBackground() throws Exception {

		try {
			ProtectedResourceClient resClient = new ProtectedResourceClient();
			String response = resClient.call(url, bearerToken);
			MessageLog.getInstance().addMessage(response);
		} catch( IOException ioe ) {
			MessageLog.getInstance().addMessage(ioe.getMessage());
			ioe.printStackTrace();
		}
		return null;
	}

}
