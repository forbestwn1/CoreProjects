package com.nosliw.app.servlet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.uiresource.HAPUIResourceManager;

public class HAPUIResourceServlet  extends HAPServiceServlet{

	private static final long serialVersionUID = 3449216679929442927L;

	public static final String COMMAND_LOADRESOURCES = "loadResource";
	public static final String COMMAND_LOADRESOURCES_NAMES = "names";
	
	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) {
		HAPServiceData out = null;

		switch(command){
		case COMMAND_LOADRESOURCES:
			HAPUIResourceManager uiResourceMan = this.getUIResourceManager();
			JSONArray names = parms.optJSONArray(COMMAND_LOADRESOURCES_NAMES);
			for(int i=0; i<names.length(); i++){
				uiResourceMan.processUIResource(names.optString(i), null);
			}
			
			break;
		}
		
		return out;
	}

	protected HAPUIResourceManager getUIResourceManager(){		return (HAPUIResourceManager)this.getServletContext().getAttribute("uiResourceManager");  }

}
