package com.nosliw.app.servlet;

import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPServiceInfo {

	@HAPAttribute
	public static final String SERVICE_COMMAND = "command";
	@HAPAttribute
	public static final String SERVICE_PARMS = "parms";
	
	private String m_command;
	private JSONObject m_parms;
	
	public HAPServiceInfo(JSONObject serviceJson) throws JSONException {
		m_command = serviceJson.getString(SERVICE_COMMAND);
		this.m_parms = serviceJson.optJSONObject(SERVICE_PARMS);
	}
	
	public String getCommand(){	return this.m_command;	}
	public JSONObject getParms(){ return this.m_parms; }
	public Object getParm(String name){ return this.m_parms.opt(name);  }
}
