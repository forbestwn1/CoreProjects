package com.nosliw.data.core.task.resource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.resource.HAPParserResourceDefinitionImp;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPManagerTask;

public class HAPParserResourceDefinitionTaskSuite extends HAPParserResourceDefinitionImp{

	private HAPManagerTask m_taskMan;
	
	public HAPParserResourceDefinitionTaskSuite(HAPManagerTask taskMan) {
		this.m_taskMan = taskMan;
	}
	
	@Override
	public HAPResourceDefinition parseJson(JSONObject jsonObj) {
		HAPResourceDefinitionTaskSuite out = new HAPResourceDefinitionTaskSuite();

		HAPUtilityComponentParse.parseComplextResourceDefinition(out, jsonObj);
		
		JSONArray taskArrayJson = jsonObj.getJSONArray(HAPWithEntityElement.ELEMENT);
		for(int i=0; i<taskArrayJson.length(); i++) {
			JSONObject taskObjJson = (JSONObject)taskArrayJson.get(i);
			String taskType = taskObjJson.getString(HAPDefinitionTask.TASKTYPE);
			HAPDefinitionTask taskDefinition = this.m_taskMan.getTaskInfo(taskType).getParser().parseTaskDefinition(taskObjJson, out);
			out.addEntityElement(taskDefinition);
		}
		return out;
	}
}
