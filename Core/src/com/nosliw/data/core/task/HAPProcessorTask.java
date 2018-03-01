package com.nosliw.data.core.task;

import java.util.Map;

import com.nosliw.data.core.HAPData;

public interface HAPProcessorTask {

	HAPExecutable process(
			HAPDefinitionTask taskDefinition, String domain, Map<String, String> variableMap,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, 
			Map<String, HAPData> contextConstants,
			HAPProcessTaskContext context
	);


	
}
