package com.nosliw.data.core.activity.plugin;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.activity.HAPPluginResourceIdActivity;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.process1.HAPActivityPluginId;
import com.nosliw.data.core.process1.HAPExecutableActivityNormal;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPEmbededTaskActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String PROCESS = "process";

	private HAPExecutableWrapperTask<HAPExecutableProcess> m_process;

	private HAPManagerActivityPlugin m_activityPluginMan;

	public HAPEmbededTaskActivityExecutable(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}

	public HAPEmbededTaskActivityExecutable(String id, HAPEmbededTaskActivityDefinition activityDef) {
		super(id, activityDef);
	}

//	public HAPExpressionActivityDefinition getExpressionActivityDefinition() {   return (HAPExpressionActivityDefinition)this.getActivityDefinition();   }
	public void setProcess(HAPExecutableWrapperTask<HAPExecutableProcess> process) {  this.m_process = process;   }
	public HAPExecutableWrapperTask<HAPExecutableProcess> getProcess() {  return this.m_process;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_process = HAPParserDataAssociation.buildExecutableWrapperTask(jsonObj, new HAPExecutableProcess(m_activityPluginMan));
		return true;  
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		dependency.add(new HAPResourceDependency(new HAPPluginResourceIdActivity(new HAPActivityPluginId(HAPConstantShared.ACTIVITY_TYPE_PROCESS))));
		this.buildResourceDependencyForExecutable(dependency, m_process, runtimeInfo, resourceManager);
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
