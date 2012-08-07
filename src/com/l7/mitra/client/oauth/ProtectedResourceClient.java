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

public class ProtectedResourceClient  {
	
	// GETs a protected resource using the supplied bearer token

	public ProtectedResourceClient() {
		
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
	public String call(String url, String bearer_token) throws IOException {
		
		
		
		URL tokenUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)tokenUrl.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		//conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		conn.setRequestProperty("Authorization", "Bearer " + bearer_token);
		
		MessageLog.getInstance().addMessage("GETting resource with bearer token: " + bearer_token);

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
	

}
