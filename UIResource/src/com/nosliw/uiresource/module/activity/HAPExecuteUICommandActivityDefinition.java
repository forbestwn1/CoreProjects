package com.nosliw.uiresource.module.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;

public class HAPExecuteUICommandActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String UI = "ui";

	@HAPAttribute
	public static String COMMAND = "command";

	@HAPAttribute
	public static String PARM = "parm";

	private String m_ui;
	
	private String m_command;
	
	private Map<String, Object> m_parms;
	
	public HAPExecuteUICommandActivityDefinition(String type) {
		super(type);
		this.m_parms = new LinkedHashMap<String, Object>();
	}

	public String getUI() {    return this.m_ui;  }
	
	public String getCommand() {   return this.m_command;  }
	public void setCommand(String command) {   this.m_command = command;   }
	
	public Map<String, Object> getParms(){  return this.m_parms;  }
	public void addParm(String name, Object parm) {   this.m_parms.put(name, parm);  }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.setCommand(jsonObj.optString(COMMAND));
		this.m_ui = (String)jsonObj.opt(UI);
		
		JSONObject parmJson = jsonObj.optJSONObject(PARM);
		if(parmJson!=null) {
			for(Object key : parmJson.keySet()) {
				this.addParm((String)key, parmJson.opt((String)key));
			}
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UI, this.m_ui);
		jsonMap.put(COMMAND, this.m_command);
		jsonMap.put(PARM, HAPJsonUtility.buildJson(this.m_parms, HAPSerializationFormat.JSON));
	}

}
