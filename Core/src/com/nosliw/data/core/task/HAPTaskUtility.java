package com.nosliw.data.core.task;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPTaskUtility {

	public static HAPDefinitionTask buildTask(Object obj, HAPManagerTask taskManager){
		HAPDefinitionTask task = null;
		try{
			JSONObject jsonObj = (JSONObject)obj;
			String type = jsonObj.optString(HAPDefinitionTask.TYPE);
			if(HAPBasicUtility.isStringEmpty(type))  type = HAPConstant.DATATASK_TYPE_EXPRESSION;
			task = taskManager.getTaskManager(type).buildTaskDefinition(obj);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return task;
	}
}
