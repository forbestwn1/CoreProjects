package com.nosliw.uiresource.module.activity;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPPresentUIActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String UI = "ui";
	
	public HAPPresentUIActivityExecutable(String id, HAPPresentUIActivityDefinition activityDef) {
		super(id, activityDef);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UI, ((HAPPresentUIActivityDefinition)this.getActivityDefinition()).getUI());
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
