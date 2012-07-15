package com.l7.mitra.client.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTextField;

public class OAuthPropertyBean {
	
	// A singleton that is useful for properties that need to be passed around between panels
	public static final String AUTHORIZATION_CODE = "azcode"; 
	private String authorizationCode;
	
	public static final String CLIENT_ID = "clientid";
	private String clientId;
	
	public static final String CLIENT_SECRET = "clientsecret";
	private String clientSecret;
	
	public static final String REDIRECT_URI = "redirecturi";
	private String redirectUri;
	
	public static final String STATE = "state";
	private String state;
	
	public static final String AZ_HOST = "authorizationhost";
	private String azHost;
	
	public static final String AZ_PORT = "authorizationport";
	private String azPort;
	
	public static final String AZ_URI = "authorizationuri";
	private String azUri;
	
	public static final String ACCESS_URI = "accessuri";
	private String accessUri;
	
	public static final String ACCESS_TOKEN = "accesstoken";
	private String accessToken;
	
	public static final String REFRESH_TOKEN = "refreshtoken";
	private String refreshToken;
	
	private String grantURL, accessURL, requestURL;

	private ArrayList propertyChangeListeners = new ArrayList();
	
	
	private static OAuthPropertyBean _instance = null;
	
	private OAuthPropertyBean() {
	}
	
	public static synchronized OAuthPropertyBean getInstance() {
		if(_instance == null) {
			_instance = new OAuthPropertyBean();
		}
		return _instance;
	}
	
	public void addChangeListener(PropertyChangeListener listener) {
		propertyChangeListeners.add(listener);
	}
	
	private void fireListenerEvent(PropertyChangeEvent evt) {
		Iterator it = propertyChangeListeners.iterator();
		while( it.hasNext() ) {
			PropertyChangeListener listener = (PropertyChangeListener)it.next();
			listener.propertyChange(evt);
		}
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		String oldValue = this.authorizationCode;
		this.authorizationCode = authorizationCode;
		// Fire an event to all listeners
		fireListenerEvent(new PropertyChangeEvent(this, AUTHORIZATION_CODE, oldValue, this.authorizationCode));
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		String oldValue = this.clientId;
		this.clientId = clientId;
		buildGrantUrl();
		fireListenerEvent(new PropertyChangeEvent(this, CLIENT_ID, oldValue, this.clientId));
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		String oldValue = this.clientSecret;
		this.clientSecret = clientSecret;
		fireListenerEvent(new PropertyChangeEvent(this, CLIENT_SECRET, oldValue, this.clientSecret));
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		String oldValue = this.redirectUri;
		this.redirectUri = redirectUri;
		buildGrantUrl();
		fireListenerEvent(new PropertyChangeEvent(this, REDIRECT_URI, oldValue, this.redirectUri));		
	}

	public String getAzHost() {
		return azHost;
	}

	public void setAzHost(String azHost) {
		String oldValue = this.azHost;
		this.azHost = azHost;
		buildGrantUrl();
		buildAccessUrl();
		fireListenerEvent(new PropertyChangeEvent(this, AZ_HOST, oldValue, this.azHost));
	}

	public String getAzPort() {
		return azPort;
	}

	public void setAzPort(String azPort) {
		String oldValue = this.azPort;
		this.azPort = azPort;
		buildGrantUrl();
		buildAccessUrl();
		fireListenerEvent(new PropertyChangeEvent(this, AZ_PORT, oldValue, this.azPort));
	}

	public String getAzUri() {
		return azUri;
	}

	public void setAzUri(String azUri) {
		String oldValue = this.azUri;
		this.azUri = azUri;
		buildGrantUrl();
		fireListenerEvent(new PropertyChangeEvent(this, AZ_URI, oldValue, this.azUri));
	}

	public String getAccessUri() {
		return accessUri;
	}

	public void setAccessUri(String accessUri) {
		String oldValue = this.accessUri;
		this.accessUri = accessUri;
		buildAccessUrl();
		fireListenerEvent(new PropertyChangeEvent(this, ACCESS_URI, oldValue, this.accessUri));
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		String oldValue = this.state;
		this.state = state;
		buildGrantUrl();
		fireListenerEvent(new PropertyChangeEvent(this, STATE, oldValue, this.state));
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		String oldValue = this.accessToken;
		this.accessToken = accessToken;
		fireListenerEvent(new PropertyChangeEvent(this, ACCESS_TOKEN, oldValue, this.accessToken));
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		String oldValue = this.refreshToken;
		this.refreshToken = refreshToken;
		fireListenerEvent(new PropertyChangeEvent(this, REFRESH_TOKEN, oldValue, this.refreshToken));
	}

	private void buildGrantUrl() {
		grantURL = "https://" + azHost + ":" + azPort+ azUri + "?response_type=code&client_id=" + clientId + "&state=" + state + "&redirect_uri=" + redirectUri;
	}
	
	private void buildAccessUrl() {
		accessURL = "https://" + azHost + ":" + azPort + accessUri;		
	}
	
	private void buildRequestUrl() {
		requestURL = "https://blah";
	}

	public String getGrantURL() {
		return grantURL;
	}

	public void setGrantURL(String grantURL) {
		this.grantURL = grantURL;
	}

	public String getAccessURL() {
		return accessURL;
	}

	
	public void setAccessURL(String accessURL) {
		this.accessURL = accessURL;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}
	
	
	
	
	
	
	
	
	

}
