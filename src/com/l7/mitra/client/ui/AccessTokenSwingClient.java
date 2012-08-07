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
	
	int grantType = -1;
	
	public AccessTokenSwingClient(int grantType) {
		this.grantType = grantType;
	}
	
	public AccessTokenSwingClient() {
		grantType = OAuthPropertyBean.getInstance().getGrantType();
	}
	
	
		@Override
	protected Object doInBackground() throws Exception {
		AccessTokenClient tokenClient = new AccessTokenClient();
		String response = "";
		
		// Get latest values from OAuthPropertyBean
		OAuthPropertyBean pBean = OAuthPropertyBean.getInstance();				
		
		try {
			if( grantType == OAuthPropertyBean.AUTHORIZATION_GRANT) {
				response = tokenClient.getAccessTokenByAuthorizationGrant(pBean.getAccessURL(), pBean.getClientId(), pBean.getClientSecret(), 
						pBean.getAuthorizationCode(), pBean.getRedirectUri(), pBean.getScope());	
			}else if( grantType == OAuthPropertyBean.PASWORD_GRANT) {
				response = tokenClient.getAccessTokenByResourceCreds(pBean.getAccessURL(), pBean.getClientId(), pBean.getClientSecret(), 
						pBean.getResUsername(), pBean.getResPassword(), pBean.getScope());
			}else if( grantType == OAuthPropertyBean.CLIENT_CREDENTIALS_GRANT) {
				response = tokenClient.getAccessTokenByClientCreds(pBean.getAccessURL(), pBean.getClientId(), pBean.getClientSecret(), pBean.getScope());
			}else if( grantType == OAuthPropertyBean.REFRESH_GRANT ) {
				response = tokenClient.refreshAccessToken(pBean.getAccessURL(), pBean.getClientId(), pBean.getClientSecret(), pBean.getRefreshToken(), 
						pBean.getRedirectUri(), pBean.getScope());
			}else {
				throw new Exception("Unknown grantType");
			}
			
			MessageLog.getInstance().addMessage(response);
			JSONObject jsonObj = new JSONObject(response.toString());
			String accessToken = jsonObj.getString("access_token");
			OAuthPropertyBean.getInstance().setAccessToken(accessToken);
			if( jsonObj.has("refresh_token") ) {
				String refreshToken = jsonObj.getString("refresh_token");
				OAuthPropertyBean.getInstance().setRefreshToken(refreshToken);
			}					
			
			
			
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
