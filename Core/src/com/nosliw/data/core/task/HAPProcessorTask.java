package com.nosliw.data.core.task;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;

public interface HAPProcessorTask {

	HAPExecutableTask process(
			HAPDefinitionTask taskDefinition, String domain, Map<String, String> variableMap,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, 
			Map<String, HAPData> contextConstants,
			HAPProcessContext context
	);

}
