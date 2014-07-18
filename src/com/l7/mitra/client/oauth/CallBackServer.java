package com.l7.mitra.client.oauth;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import NanoHTTPD.NanoHTTPD;
import NanoHTTPD.NanoHTTPD.Response;

import com.l7.mitra.client.ui.MessageLog;
import com.l7.mitra.client.ui.OAuthPropertyBean;
import com.l7.mitra.client.ui.panels.AuthorizationPanel;


public class CallBackServer extends NanoHTTPD  {

	int port;
	
	public CallBackServer(int port) throws IOException {	
		super(port, new File("."));
		this.port = port;
		MessageLog.getInstance().addMessage("Call back server listening on port " + port);
	}
	
	
	
	
	@Override
	public Response serve(String uri, String method, Properties header,
			Properties parms, String postData, Properties files) {
		// TODO Auto-generated method stub
		MessageLog.getInstance().addMessage("Received request for URI " + uri);
		
		
		
		if( method.compareTo("GET") == 0 && uri.compareTo("/callback") == 0) {
			// Grab the code parameter from the request
			// TODO: validate the state parameter
			System.out.println(uri);
			String state = parms.getProperty("state");
			String scope = parms.getProperty("scope");
			String code = parms.getProperty("code");
			if( state != null &&  state.compareTo(OAuthPropertyBean.getInstance().getState()) != 0) {
				MessageLog.getInstance().addMessage("*** WARNING: Authorization response state field mismatch.  This may be a CSRF attack! ***" );
			}
			OAuthPropertyBean.getInstance().setAuthorizationCode(code);
			MessageLog.getInstance().addMessage("Authorization code succesfully received.");
			return new Response("200","text/html", "<HTML><h1>Authorization granted!</h1><p/>Return to the OAuthTestClient to retrieve an access code.</HTML>");
		}else {
			return new Response("404", "text/html", "Not Found.");
		}
		
	}


}
