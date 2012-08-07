package com.l7.mitra.client.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

	public ConfigManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static void loadProperties(File file) {
		
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			MessageLog.getInstance().addMessage("Unable to load properties: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			MessageLog.getInstance().addMessage("Unable to load properties: " + e.getMessage());
		}
		
		OAuthPropertyBean pBean = OAuthPropertyBean.getInstance();
		pBean.setAzHost(props.getProperty(OAuthPropertyBean.AZ_HOST, ""));
		pBean.setAzPort(props.getProperty(OAuthPropertyBean.AZ_PORT, ""));
		pBean.setAzUri(props.getProperty(OAuthPropertyBean.AZ_URI, ""));
		pBean.setClientId(props.getProperty(OAuthPropertyBean.CLIENT_ID, ""));
		pBean.setClientSecret(props.getProperty(OAuthPropertyBean.CLIENT_SECRET, ""));
		pBean.setScope(props.getProperty(OAuthPropertyBean.SCOPE, ""));
		pBean.setState(props.getProperty(OAuthPropertyBean.STATE, ""));
		pBean.setAccessUri(props.getProperty(OAuthPropertyBean.ACCESS_URI, ""));
	}
	
	public static void saveProperties(File file) {
		Properties props = new Properties();
		OAuthPropertyBean pBean = OAuthPropertyBean.getInstance();
		props.put(OAuthPropertyBean.AZ_HOST, pBean.getAzHost());
		props.put(OAuthPropertyBean.AZ_PORT, pBean.getAzPort());
		props.put(OAuthPropertyBean.AZ_URI, pBean.getAzUri());
		props.put(OAuthPropertyBean.CLIENT_ID, pBean.getClientId());
		props.put(OAuthPropertyBean.CLIENT_SECRET, pBean.getClientSecret());
		props.put(OAuthPropertyBean.SCOPE, pBean.getScope());
		props.put(OAuthPropertyBean.STATE, pBean.getState());
		props.put(OAuthPropertyBean.ACCESS_URI, pBean.getAccessUri());		
		
		try {
			props.store(new FileOutputStream(file), "OAuthTestClient Configuration File");
		} catch (FileNotFoundException e) {
			MessageLog.getInstance().addMessage("Unable to save properties: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			MessageLog.getInstance().addMessage("Unable to save properties: " + e.getMessage());
		}
	}

}
