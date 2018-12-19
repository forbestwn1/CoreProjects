package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.process.activity.HAPEndActivityPlugin;
import com.nosliw.data.core.process.activity.HAPExpressionActivityPlugin;
import com.nosliw.data.core.process.activity.HAPStartActivityPlugin;
import com.nosliw.data.core.script.context.HAPContext;

public class HAPManagerProcess {

	Map<String, HAPPluginActivity> m_activityPlugins;
	
	public HAPManagerProcess() {
		this.m_activityPlugins = new LinkedHashMap<String, HAPPluginActivity>();
		
		this.registerActivityPlugin(new HAPStartActivityPlugin());
		this.registerActivityPlugin(new HAPEndActivityPlugin());
		this.registerActivityPlugin(new HAPExpressionActivityPlugin());
		
	}
	
	public HAPExecutableProcess getProcess(String processId) {
		return null;
	}
	
	public HAPExecutableProcess compileProcess(
			String id,
			HAPDefinitionProcess processDefinition, 
			Map<String, HAPDefinitionProcess> contextProcessDefinitions, 
			HAPContext parentContext, 
			Map<String, String> configure,
			HAPProcessContext context) {
//		HAPExecutableProcess task = processTask(processDefinition, null, null, contextProcessDefinitions, constants, context);
//		task.setId(id);
//		
//		Map<String, HAPVariableInfo> variableInfos = parentVariablesInfo;
//		if(variableInfos==null) 	variableInfos = new LinkedHashMap<String, HAPVariableInfo>();
//		task.discoverVariable(variableInfos, expectOutput, context);
//		
//		return task;
		return null;
	}
	
	public HAPExecutableProcess processTask(
			HAPDefinitionProcess taskDefinition, 
			String domain, 
			HAPContext variableMap,
			Map<String, HAPDefinitionProcess> contextTaskDefinitions,
			HAPProcessContext context) {
//		HAPExecutableProcess out = HAPProcessorProcess.process(taskDefinition, domain, variableMap, contextTaskDefinitions, context, this);
//		return out;
		return null;
	}

	public void registerActivityPlugin(HAPPluginActivity plugin) {		this.m_activityPlugins.put(plugin.getType(), plugin);	}
	
	public HAPPluginActivity getActivityPlugin(String activityType) {		return this.m_activityPlugins.get(activityType);	}
	
}
