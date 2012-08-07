package com.l7.mitra.client.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.l7.mitra.client.ui.panels.AccessPanel;
import com.l7.mitra.client.ui.panels.AuthorizationPanel;
import com.l7.mitra.client.ui.panels.ClientSettingsPanel;
import com.l7.mitra.client.ui.panels.OAuthTestPanel;
import com.l7.mitra.client.ui.panels.RefreshPanel;
import com.l7.mitra.client.ui.panels.RequestPanel;
import com.l7.mitra.client.ui.panels.ResourceOwnerSettingsPanel;
import com.l7.mitra.client.ui.panels.ServerSettingsPanel;

public class GrantStepsListModel implements ListModel<String>, PropertyChangeListener {
	
	private int grantType = 0;
	OAuthTestPanel p_serverSettings = new ServerSettingsPanel();
	OAuthTestPanel p_clientSettings = new ClientSettingsPanel();
	OAuthTestPanel p_authorize = new AuthorizationPanel();	
	OAuthTestPanel p_access = new AccessPanel();
	OAuthTestPanel p_refresh = new RefreshPanel();
	OAuthTestPanel p_request = new RequestPanel();
	OAuthTestPanel p_resourceOwner = new ResourceOwnerSettingsPanel();
	
	private OAuthTestPanel[] stepsArray;
	private final OAuthTestPanel[] passwordGrantSteps = { p_serverSettings, p_clientSettings, p_resourceOwner, p_access, p_refresh, p_request };
	private final OAuthTestPanel[] authorizationGrantSteps = { p_serverSettings, p_clientSettings, p_authorize, p_access, p_refresh, p_request };
	private final OAuthTestPanel[] clientGrantSteps = { p_serverSettings, p_clientSettings, p_access, p_request };
		
	ArrayList<ListDataListener> dataListeners = new ArrayList<ListDataListener>();

	public GrantStepsListModel() {
		setGrantType(OAuthPropertyBean.AUTHORIZATION_GRANT);
		OAuthPropertyBean.getInstance().addChangeListener(this);
	}

	@Override
	public void addListDataListener(ListDataListener arg0) {
		dataListeners.add(arg0);
		
	}

	@Override
	public String getElementAt(int arg0) {
		// TODO Auto-generated method stub
		String stepValue = arg0 + 1 + ". " + stepsArray[arg0].getPanelDescription();
		return stepValue;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return stepsArray.length;
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		dataListeners.remove(arg0);		
	}
	
	private void setGrantType(int grantType) {
		this.grantType = grantType;
		switch(grantType) {
			case OAuthPropertyBean.AUTHORIZATION_GRANT : stepsArray = authorizationGrantSteps; break;
			case OAuthPropertyBean.PASWORD_GRANT : stepsArray = passwordGrantSteps; break;
			case OAuthPropertyBean.CLIENT_CREDENTIALS_GRANT : stepsArray = clientGrantSteps; break;
		}
		Iterator<ListDataListener> it = dataListeners.iterator();
		while( it.hasNext()) {
			it.next().contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, stepsArray.length));
		}
		
	}
	
	public OAuthTestPanel getPanel(int index) {
		return stepsArray[index];
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if( evt.getPropertyName() == OAuthPropertyBean.GRANTTYPE) {
			setGrantType((Integer) evt.getNewValue());
		}
		
	}
	
}
