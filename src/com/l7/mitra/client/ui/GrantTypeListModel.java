package com.l7.mitra.client.ui;

import java.util.ArrayList;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class GrantTypeListModel implements ListModel<OAuthStepPanel> {

	public static int AUTHORIZATION_CODE = 0;
	public static int IMPLICIT = 1;
	private int grantType = AUTHORIZATION_CODE;
	private ArrayList stepPanels = new ArrayList();
	
	public void setGrantType(int grantType) {
		this.grantType = grantType;
	}
	
	@Override
	public void addListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OAuthStepPanel getElementAt(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
