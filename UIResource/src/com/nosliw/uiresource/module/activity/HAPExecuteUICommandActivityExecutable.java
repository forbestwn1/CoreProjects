package com.nosliw.uiresource.module.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPExecuteUICommandActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String PARTID = "partId";
 
	@HAPAttribute
	public static String COMMAND = "command";

	private String m_componentId;
	
	private String m_command;
	
	public HAPExecuteUICommandActivityExecutable(String id, HAPExecuteUICommandActivityDefinition activityDef) {
		super(id, activityDef);
		this.m_command = activityDef.getCommand();
		this.m_componentId = activityDef.getComponentId();
	}

//	private HAPExecuteUICommandActivityDefinition getDefinition() {   return (HAPExecuteUICommandActivityDefinition)this.getActivityDefinition(); }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_command = jsonObj.getString(COMMAND);
		this.m_componentId = jsonObj.getString(PARTID);
		return true;  
	}

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PARTID, this.m_componentId);
		jsonMap.put(COMMAND, this.m_command);
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPResourceIdActivityPlugin(new HAPActivityPluginId("UI_executeCommand"))));
		return out;
	}

}
