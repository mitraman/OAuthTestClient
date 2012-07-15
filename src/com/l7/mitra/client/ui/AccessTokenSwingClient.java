package com.l7.mitra.client.ui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSession;
import javax.swing.SwingWorker;
import javax.xml.bind.DatatypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

import com.l7.mitra.client.oauth.AccessTokenClient;

public class AccessTokenSwingClient extends SwingWorker {
	
	String url = "";
	String client_key = "";
	String client_secret = "";
	String authorizationCode = "";
	String redirect_uri = "";
	String refreshToken= "";
	int grantType = -1;
	public static final int REFRESH_TOKEN = 0;
	public static final int ACCESS_TOKEN = 1;

	public AccessTokenSwingClient(int grantType) {
		this.grantType = grantType;
	}
		@Override
	protected Object doInBackground() throws Exception {
		AccessTokenClient tokenClient = new AccessTokenClient();
		String response = "";
		try {
			if( grantType == ACCESS_TOKEN) {
				if( client_secret == null || client_secret.trim().isEmpty()) {
					response = tokenClient.getAccessToken(url, client_key, authorizationCode, redirect_uri);
				}else {
					response = tokenClient.getAccessToken(url, client_key, client_secret, authorizationCode, redirect_uri);	
				}
				
			}
			else if( grantType == REFRESH_TOKEN )response = tokenClient.refreshAccessToken(url, client_key, client_secret, refreshToken, redirect_uri);
			else throw new Exception("Unknown grantType");
			MessageLog.getInstance().addMessage(response);
			JSONObject jsonObj = new JSONObject(response.toString());
			String accessToken = jsonObj.getString("access_token");
			String refreshToken = jsonObj.getString("refresh_token");		
			OAuthPropertyBean.getInstance().setAccessToken(accessToken);
			OAuthPropertyBean.getInstance().setRefreshToken(refreshToken);
			
		}catch (IOException ioe ) {
			ioe.printStackTrace();
			MessageLog.getInstance().addMessage(ioe.getMessage());
		}catch (JSONException e) {
			e.printStackTrace();
			MessageLog.getInstance().addMessage(e.getMessage());
		}
				
		return null;
	}
}
