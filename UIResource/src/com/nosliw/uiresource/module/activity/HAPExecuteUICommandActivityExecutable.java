package com.nosliw.uiresource.module.activity;

import java.util.List;
import java.util.Map;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPExecuteUICommandActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String COMMAND = "command";

	@HAPAttribute
	public static String PARM = "parm";

	public HAPExecuteUICommandActivityExecutable(String id, HAPExecuteUICommandActivityDefinition activityDef) {
		super(id, activityDef);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
//		jsonMap.put(COMMAND, this.m_command);
//		jsonMap.put(PARM, HAPJsonUtility.buildJson(this.m_parms, HAPSerializationFormat.JSON));
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
