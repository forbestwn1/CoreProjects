package com.nosliw.data.core.datasource.task;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPExecutableTask;
import com.nosliw.data.core.task.HAPProcessorTask;

public class HAPProcessorTaskDataSource implements HAPProcessorTask{

	@Override
	public HAPExecutableTask process(HAPDefinitionTask taskDefinition, String domain, Map<String, String> variableMap,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants,
			HAPProcessContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
