package com.nosliw.data.core.task;

import org.json.JSONObject;

import com.nosliw.data.core.component.HAPDefinitionEntityComplex;

public class HAPUtilityTask {

	public static HAPDefinitionTask parseTask(JSONObject jsonObj, HAPDefinitionEntityComplex complexEntity, HAPManagerTask taskMan) {
		String taskType = jsonObj.getString(HAPDefinitionTask.TASKTYPE);
		HAPDefinitionTask taskDefinition = taskMan.getTaskInfo(taskType).getParser().parseTaskDefinition(jsonObj, complexEntity);
		return taskDefinition;
	}
	
}
