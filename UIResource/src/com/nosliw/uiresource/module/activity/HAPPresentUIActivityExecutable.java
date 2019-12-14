package com.nosliw.uiresource.module.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPPresentUIActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String UI = "ui";

	@HAPAttribute
	public static String SETTING = "setting";

	public HAPPresentUIActivityExecutable(String id, HAPPresentUIActivityDefinition activityDef) {
		super(id, activityDef);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		HAPPresentUIActivityDefinition activity = (HAPPresentUIActivityDefinition)this.getActivityDefinition();
		jsonMap.put(UI, activity.getUI());
		jsonMap.put(SETTING, HAPJsonUtility.buildJson(activity.getSetting(), HAPSerializationFormat.JSON));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPResourceIdActivityPlugin(new HAPActivityPluginId("UI_presentUI"))));
		return out;
	}
}
