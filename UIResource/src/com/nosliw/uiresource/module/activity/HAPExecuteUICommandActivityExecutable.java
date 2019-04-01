package com.nosliw.uiresource.module.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.resource.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPExecuteUICommandActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String UI = "ui";
 
	@HAPAttribute
	public static String COMMAND = "command";

	@HAPAttribute
	public static String PARM = "parm";

	public HAPExecuteUICommandActivityExecutable(String id, HAPExecuteUICommandActivityDefinition activityDef) {
		super(id, activityDef);
	}

	private HAPExecuteUICommandActivityDefinition getDefinition() {   return (HAPExecuteUICommandActivityDefinition)this.getActivityDefinition(); }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UI, this.getDefinition().getUI());
		jsonMap.put(COMMAND, this.getDefinition().getCommand());
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		out.add(new HAPResourceDependent(new HAPResourceIdActivityPlugin(new HAPActivityPluginId("UI_executeUICommand"))));
		return out;
	}

}
