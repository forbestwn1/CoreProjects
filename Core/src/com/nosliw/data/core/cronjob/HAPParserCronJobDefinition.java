package com.nosliw.data.core.cronjob;

import org.json.JSONObject;

import com.nosliw.data.core.component.HAPUtilityComponent;

public class HAPParserCronJobDefinition {

	public HAPDefinitionCronJob parseFile(String fileName){
		return null;
	}
	
	public HAPDefinitionCronJob parsePocessCronJob(JSONObject cronJobJson) {
		HAPDefinitionCronJob out = new HAPDefinitionCronJob();
		
		HAPUtilityComponent.parseComponent(out, cronJobJson);
		
		
		
		
		return out;
	}
	
	private HAPDefinitionSchedule parseSchedule(JSONObject schedulJson) {
		
	}
}
