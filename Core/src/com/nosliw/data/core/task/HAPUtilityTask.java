package com.nosliw.data.core.task;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.division.manual.HAPManualEntityComplex;

public class HAPUtilityTask {

	public static HAPDefinitionTask parseTask(JSONObject jsonObj, HAPManualEntityComplex complexEntity, HAPManagerTask taskMan) {
		if(!HAPUtilityEntityInfo.isEnabled(jsonObj))   return null;
		
		String taskType = jsonObj.getString(HAPDefinitionTask.TASKTYPE);
		HAPDefinitionTask taskDefinition = taskMan.getTaskInfo(taskType).getParser().parseTaskDefinition(jsonObj, complexEntity);
		return taskDefinition;
	}
	
}
