package com.nosliw.uiresource.module.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	public HAPExecuteUICommandActivityExecutable(String id, HAPExecuteUICommandActivityDefinition activityDef) {
		super(id, activityDef);
	}

	private HAPExecuteUICommandActivityDefinition getDefinition() {   return (HAPExecuteUICommandActivityDefinition)this.getActivityDefinition(); }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PARTID, this.getDefinition().getComponentId());
		jsonMap.put(COMMAND, this.getDefinition().getCommand());
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPResourceIdActivityPlugin(new HAPActivityPluginId("UI_executeCommand"))));
		return out;
	}

}
