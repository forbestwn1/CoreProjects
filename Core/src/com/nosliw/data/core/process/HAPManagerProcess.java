package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.plugin.HAPPluginActivity;
import com.nosliw.data.core.script.context.HAPContext;

public class HAPManagerProcess {
	
	private HAPManagerActivityPlugin m_pluginManager;
	
	public HAPManagerProcess(HAPManagerActivityPlugin pluginMan) {
		this.m_pluginManager = pluginMan;
	}

	public HAPManagerProcess() {
		this.m_pluginManager = new HAPManagerActivityPlugin();
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

	public HAPManagerActivityPlugin getPluginManager() {   return this.m_pluginManager;    }
	
}
