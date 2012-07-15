package com.l7.mitra.client.oauth;

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

import com.l7.mitra.client.ui.AuthorizationPanel;
import com.l7.mitra.client.ui.MessageLog;
import com.l7.mitra.client.ui.OAuthPropertyBean;


public class CallBackServer extends NanoHTTPD {

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
			String code = parms.getProperty("code");
			OAuthPropertyBean.getInstance().setAuthorizationCode(code);
			MessageLog.getInstance().addMessage("Authorization code succesfully received.");
			return new Response("200","text/html", "<HTML><h1>Authorization granted!</h1><p/>Return to the OAuthTestClient to retrieve an access code.</HTML>");
		}else {
			return new Response("404", "text/html", "Not Found.");
		}
		
	}




	
	


}
