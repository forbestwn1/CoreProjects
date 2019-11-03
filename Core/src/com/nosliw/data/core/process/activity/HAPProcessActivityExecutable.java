package com.nosliw.data.core.process.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.resource.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;

public class HAPProcessActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String PROCESS = "process";

	private HAPExecutableWrapperTask m_process;
	
	public HAPProcessActivityExecutable(String id, HAPProcessActivityDefinition activityDef) {
		super(id, activityDef);
	}

	public HAPExpressionActivityDefinition getExpressionActivityDefinition() {   return (HAPExpressionActivityDefinition)this.getActivityDefinition();   }
	public void setProcess(HAPExecutableWrapperTask process) {  this.m_process = process;   }
	public HAPExecutableWrapperTask getProcess() {  return this.m_process;   }
	
	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		out.add(new HAPResourceDependent(new HAPResourceIdActivityPlugin(new HAPActivityPluginId(HAPConstant.ACTIVITY_TYPE_PROCESS))));
		out.addAll(this.m_process.getResourceDependency(runtimeInfo));
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROCESS, this.m_process.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(PROCESS, this.m_process.toResourceData(runtimeInfo).toString());
	}	
}
