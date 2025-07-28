package com.nosliw.data.core.task.resource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.component.HAPParserEntityComponent;
import com.nosliw.data.core.resource.HAPParserResourceEntityImp;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.core.task.HAPUtilityTask;

public class HAPParserResourceDefinitionTaskSuite extends HAPParserResourceEntityImp{

	private HAPManagerTask m_taskMan;
	
	public HAPParserResourceDefinitionTaskSuite(HAPManagerTask taskMan) {
		this.m_taskMan = taskMan;
	}
	
	@Override
	public HAPResourceDefinition1 parseJson(JSONObject jsonObj) {
		HAPResourceDefinitionTaskSuite out = new HAPResourceDefinitionTaskSuite();

		HAPParserEntityComponent.parseComplextResourceDefinition(out, jsonObj);
		
		JSONArray taskArrayJson = jsonObj.getJSONArray(HAPWithEntityElement.ELEMENT);
		for(int i=0; i<taskArrayJson.length(); i++) {
			JSONObject taskObjJson = (JSONObject)taskArrayJson.get(i);
			HAPDefinitionTask taskDefinition = HAPUtilityTask.parseTask(taskObjJson, out, m_taskMan);
			out.addEntityElement(taskDefinition);
		}
		return out;
	}
}
