package com.nosliw.uiresource.module.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;

public class HAPExecuteUICommandActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String PARTID = "partId";

	@HAPAttribute
	public static String COMMAND = "command";

	private String m_componentId;
	
	private String m_command;
	
	public HAPExecuteUICommandActivityDefinition(String type) {
		super(type);
	}

	public String getComponentId() {    return this.m_componentId;  }
	
	public String getCommand() {   return this.m_command;  }
	public void setCommand(String command) {   this.m_command = command;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.setCommand(jsonObj.optString(COMMAND));
		this.m_componentId = (String)jsonObj.opt(PARTID);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PARTID, this.m_componentId);
		jsonMap.put(COMMAND, this.m_command);
	}

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPExecuteUICommandActivityDefinition out = new HAPExecuteUICommandActivityDefinition(this.getActivityType());
		this.cloneToNormalActivityDefinition(out);
		out.m_componentId = this.m_componentId;
		out.m_command = this.m_command;
		return out;
	}

}
