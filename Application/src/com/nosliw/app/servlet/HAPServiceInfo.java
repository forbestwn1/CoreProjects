package com.nosliw.app.servlet;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

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
	private Map<String, Object> m_parms;
	
	public HAPServiceInfo(JSONObject serviceJson) throws JSONException {
		m_command = serviceJson.getString(SERVICE_COMMAND);
		this.m_parms = new LinkedHashMap<String, Object>();
		JSONObject parmsJsonObj = serviceJson.optJSONObject(SERVICE_PARMS);
		if(parmsJsonObj!=null){
			Iterator keys = parmsJsonObj.keys();
			while(keys.hasNext()){
				String key = (String)keys.next();
				this.m_parms.put(key, parmsJsonObj.get(key));
			}
		}
	}
	
	public String getCommand(){	return this.m_command;	}
	public Map<String, Object> getParms(){ return this.m_parms; }
	public Object getParm(String name){ return this.m_parms.get(name);  }
}
