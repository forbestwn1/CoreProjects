package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPContextGroup;

public class HAPManagerTask {

	private Map<String, HAPPluginActivity> m_stepPlugs;
	
	public HAPManagerTask(HAPRuntime runtime) {
		this.m_stepPlugs = new LinkedHashMap<String, HAPPluginActivity>();
	}
	
	
	public HAPPluginActivity getStepPlug(String type) {
		return this.m_stepPlugs.get(type);
	}
	
	public HAPServiceData executeTask(
			String taskName, 
			HAPDefinitionProcessSuite taskSuite) {
		
		
		return null;
	}
	
	
//	public HAPExecutableTask compileTask(
//			String taskName, 
//			HAPDefinitionTaskSuite taskSuite,
//			Map<String, String> configure,
//			HAPProcessContext context) {
//
//		
//		HAPExecutableTask task = processTask(taskDefinition, null, null, contextTaskDefinitions, contextConstants, context);
//		task.setId(id);
//		
//		Map<String, HAPVariableInfo> variableInfos = parentVariablesInfo;
//		if(variableInfos==null) 	variableInfos = new LinkedHashMap<String, HAPVariableInfo>();
//		task.discoverVariable(variableInfos, expectOutput, context);
//		
//		return task;
//	}
//	
//	public HAPExecutableTask compileTask(
//			String id,
//			HAPDefinitionTask taskDefinition, 
//			Map<String, HAPDefinitionTask> contextTaskDefinitions,
//			HAPContextGroup suiteContext,
//			Map<String, String> configure,
//			HAPProcessContext context) {
//
//		
//		HAPExecutableTask task = processTask(taskDefinition, null, null, contextTaskDefinitions, contextConstants, context);
//		task.setId(id);
//		
//		Map<String, HAPVariableInfo> variableInfos = parentVariablesInfo;
//		if(variableInfos==null) 	variableInfos = new LinkedHashMap<String, HAPVariableInfo>();
//		task.discoverVariable(variableInfos, expectOutput, context);
//		
//		return task;
//	}
	
}
