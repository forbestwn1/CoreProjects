package com.nosliw.data.core.process.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPDebugActivityExecutable extends HAPExecutableActivityNormal{

	public HAPDebugActivityExecutable(String id, HAPDebugActivityDefinition activityDef) {
		super(id, activityDef);
	}

//	public HAPDebugActivityDefinition getDebugActivityDefinition() {   return (HAPDebugActivityDefinition)this.getActivityDefinition();   }
	
	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPResourceIdActivityPlugin(new HAPActivityPluginId(HAPConstant.ACTIVITY_TYPE_PROCESS))));
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
	}	
}
