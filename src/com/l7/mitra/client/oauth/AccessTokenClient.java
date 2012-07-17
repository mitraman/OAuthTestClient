package com.l7.mitra.client.oauth;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
import org.json.JSONTokener;

import com.l7.mitra.client.ui.MessageLog;


public class AccessTokenClient  {
	
	

	public AccessTokenClient() {
		
		// *** WARNING: I am turning off client side validation of the server's SSL certificate to make my 
		// life easier.  ***  
		
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}

		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String string, SSLSession ssls) {
				return true;
			}
		});

	}
	public String refreshAccessToken(String url, String client_key, String client_secret, String refreshToken, String redirect_uri) throws IOException {
		
		String urlParams = "grant_type=refresh_token&refresh_token=" + URLEncoder.encode(refreshToken, "UTF-8") +
		        "&redirect_uri=" + URLEncoder.encode(redirect_uri, "UTF-8");
		return call(url, client_key, client_secret, urlParams, true);
	}
	
	public String getAccessToken(String url, String client_key, String client_secret, String authorizationCode, String redirect_uri) throws IOException {
		
		String urlParams = "grant_type=authorization_code&code=" + URLEncoder.encode(authorizationCode, "UTF-8") +
		        "&redirect_uri=" + URLEncoder.encode(redirect_uri, "UTF-8");
		return call(url, client_key, client_secret, urlParams, true);
	}
	
public String getAccessToken(String url, String client_key, String authorizationCode, String redirect_uri) throws IOException {
		
		String urlParams = "grant_type=authorization_code&code=" + URLEncoder.encode(authorizationCode, "UTF-8") +
		        "&redirect_uri=" + URLEncoder.encode(redirect_uri, "UTF-8");
		return call(url, client_key, null, urlParams, false);
	}
	
	private String call(String url, String client_key, String client_secret, String urlParams, boolean confidential) throws IOException {
		
		URL tokenUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)tokenUrl.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		
		
		 if( confidential) {
				// Set basic authorization header
				String authString = client_key + ":" + client_secret;		
				String encAuthString = DatatypeConverter.printBase64Binary(authString.getBytes());		
				conn.setRequestProperty("Authorization", "Basic " + encAuthString);			 
		 }else {
			 // This is a public client, so just send the client id for auditing purposes
			 urlParams = urlParams.concat("&client_id="+URLEncoder.encode(client_key, "UTF-8"));
		 }
		 
		 MessageLog.getInstance().addMessage("POSTing " + url + "?" + urlParams);
		
		 //Send request
	      DataOutputStream wr = new DataOutputStream (
	    		  conn.getOutputStream ());
	      wr.writeBytes (urlParams);
	      wr.flush ();
	      wr.close ();


	      //Get Response	
	      InputStream is = conn.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      
	    
	      return response.toString();
	      
	      
	}
	
	public static void main(String args[]) {
		String client_key = "73fee98f-5341-463d-968e-c553f26375cd";
		String client_secret = "4d3a675e-7445-4329-a08a-c51bb636bed2";	
		String redirect_uri = "http://localhost:8080/callback";
		String authorizationCode = "48baa318-1d0c-45de-bf51-3d52366f14fc";
		String url = "https://192.168.44.129:8443/auth/oauth/v2/token";
		
		AccessTokenClient tokenClient = new AccessTokenClient();
		try {
			String response = tokenClient.getAccessToken(url, client_key, client_secret, authorizationCode, redirect_uri);
			System.out.println(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
