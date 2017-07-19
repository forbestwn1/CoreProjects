package com.nosliw.app.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.app.utils.HAPAttributeConstant;

public class HAPServiceInfo {

	private String m_command;
	private JSONObject m_parms;
	
	public HAPServiceInfo(JSONObject serviceJson) throws JSONException {
		m_command = serviceJson.getString(HAPAttributeConstant.SERVICE_COMMAND);
		m_parms = serviceJson.getJSONObject(HAPAttributeConstant.SERVICE_PARMS);
	}
	
	public String getCommand(){	return this.m_command;	}
	public JSONObject getParmsJson(){ return this.m_parms; }
}
