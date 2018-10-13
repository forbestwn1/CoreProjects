package com.nosliw.data.core.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.script.context.HAPContext;

public class HAPManagerFlowTask {

	Map<String, HAPPluginStep> m_stepPlugins;
	
	public HAPManagerFlowTask() {
		this.m_stepPlugins = new LinkedHashMap<String, HAPPluginStep>();
	}
	
	public HAPExecutableTask compileTask(
			String id,
			HAPDefinitionTask taskDefinition, 
			Map<String, HAPDefinitionTask> contextTaskDefinitions, 
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			Map<String, HAPData> constants,
			HAPDataTypeCriteria expectOutput,
			Map<String, String> configure,
			HAPProcessContext context) {
		HAPExecutableTask task = processTask(taskDefinition, null, null, contextTaskDefinitions, constants, context);
		task.setId(id);
		
		Map<String, HAPVariableInfo> variableInfos = parentVariablesInfo;
		if(variableInfos==null) 	variableInfos = new LinkedHashMap<String, HAPVariableInfo>();
		task.discoverVariable(variableInfos, expectOutput, context);
		
		return task;
	}
	
	public HAPExecutableTask processTask(HAPDefinitionTask taskDefinition, String domain, HAPContext variableMap,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants,
			HAPProcessContext context) {
		HAPExecutableTask out = HAPProcessorTask.process(taskDefinition, domain, variableMap, contextTaskDefinitions, contextConstants, context, this);
		return out;
	}

	public void registerStepPlugin(String stepType, HAPPluginStep plugin) {
		this.m_stepPlugins.put(stepType, plugin);
	}
	
	public HAPPluginStep getStepPlugin(String stepType) {
		return this.m_stepPlugins.get(stepType);
	}
	
}
