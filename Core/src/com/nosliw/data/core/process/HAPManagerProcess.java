package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.script.context.HAPContext;

public class HAPManagerProcess {

	Map<String, HAPPluginActivity> m_activityPlugins;
	
	public HAPManagerProcess() {
		this.m_activityPlugins = new LinkedHashMap<String, HAPPluginActivity>();
	}
	
	public HAPExecutableProcess compileProcess(
			String id,
			HAPDefinitionProcess processDefinition, 
			Map<String, HAPDefinitionProcess> contextProcessDefinitions, 
			HAPContext parentContext, 
			Map<String, String> configure,
			HAPProcessContext context) {
		HAPExecutableProcess task = processTask(processDefinition, null, null, contextProcessDefinitions, constants, context);
		task.setId(id);
		
		Map<String, HAPVariableInfo> variableInfos = parentVariablesInfo;
		if(variableInfos==null) 	variableInfos = new LinkedHashMap<String, HAPVariableInfo>();
		task.discoverVariable(variableInfos, expectOutput, context);
		
		return task;
	}
	
	public HAPExecutableProcess processTask(
			HAPDefinitionProcess taskDefinition, 
			String domain, 
			HAPContext variableMap,
			Map<String, HAPDefinitionProcess> contextTaskDefinitions,
			HAPProcessContext context) {
		HAPExecutableProcess out = HAPProcessorProcess.process(taskDefinition, domain, variableMap, contextTaskDefinitions, context, this);
		return out;
	}

	public void registerActivityPlugin(String activityType, HAPPluginActivity plugin) {		this.m_activityPlugins.put(activityType, plugin);	}
	
	public HAPPluginActivity getActivityPlugin(String activityType) {		return this.m_activityPlugins.get(activityType);	}
	
}
