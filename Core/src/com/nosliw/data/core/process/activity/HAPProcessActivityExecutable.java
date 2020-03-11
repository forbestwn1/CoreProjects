package com.nosliw.data.core.process.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPParserDataAssociation;

public class HAPProcessActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String PROCESS = "process";

	private HAPExecutableWrapperTask<HAPExecutableProcess> m_process;

	private HAPManagerActivityPlugin m_activityPluginMan;

	public HAPProcessActivityExecutable(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}

	public HAPProcessActivityExecutable(String id, HAPProcessActivityDefinition activityDef) {
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
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPResourceIdActivityPlugin(new HAPActivityPluginId(HAPConstant.ACTIVITY_TYPE_PROCESS))));
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
