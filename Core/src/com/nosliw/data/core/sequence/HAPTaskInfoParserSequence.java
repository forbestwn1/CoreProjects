package com.nosliw.data.core.sequence;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.division.manual.HAPManualEntityComplex;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.core.task.HAPParserTask;
import com.nosliw.data.core.task.HAPUtilityTask;

public class HAPTaskInfoParserSequence implements HAPParserTask{

	private HAPManagerTask m_taskMan;
	
	public HAPTaskInfoParserSequence(HAPManagerTask taskMan) {
		this.m_taskMan = taskMan;
	}

	@Override
	public HAPDefinitionTask parseTaskDefinition(Object obj, HAPManualEntityComplex complexEntity) {
		HAPDefinitionSequence out = new HAPDefinitionSequence();
		
		JSONObject jsonObj = (JSONObject)obj;
		out.buildEntityInfoByJson(jsonObj);
		JSONArray stepsArray = jsonObj.getJSONArray(HAPDefinitionSequence.STEP);
		
		for(int i=0; i<stepsArray.length(); i++) {
			JSONObject stepJson = stepsArray.getJSONObject(i);
			 out.addStep(HAPUtilityTask.parseTask(stepJson, complexEntity, m_taskMan));
		}
		
		return out;	
	}

}
