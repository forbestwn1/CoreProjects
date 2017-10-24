package com.nosliw.app.servlet;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.uiresource.HAPUIResource;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitResource;

public class HAPUIResourceServlet  extends HAPServiceServlet{

	private static final long serialVersionUID = -8585224614205786036L;

	public static final String COMMAND_LOADRESOURCES = "loadResource";
	public static final String COMMAND_LOADRESOURCES_NAMES = "names";
	
	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) {
		HAPServiceData out = null;

		switch(command){
		case COMMAND_LOADRESOURCES:
			HAPUIResourceManager uiResourceMan = this.getUIResourceManager();
			JSONArray names = parms.optJSONArray(COMMAND_LOADRESOURCES_NAMES);
			Map<String, HAPUIDefinitionUnitResource> resources = new LinkedHashMap<String, HAPUIDefinitionUnitResource>();
			for(int i=0; i<names.length(); i++){
				HAPUIDefinitionUnitResource resource = uiResourceMan.getUIResource(names.optString(i));
				resources.put(names.optString(i), resource);
			}
			out = HAPServiceData.createSuccessData(resources);
			break;
		}
		
		return out;
	}

	protected HAPUIResourceManager getUIResourceManager(){		return (HAPUIResourceManager)this.getServletContext().getAttribute("uiResourceManager");  }

}
