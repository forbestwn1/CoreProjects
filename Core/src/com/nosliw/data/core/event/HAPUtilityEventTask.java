package com.nosliw.data.core.event;

import com.nosliw.common.utils.HAPFileUtility;

public class HAPUtilityEventTask {

	public static HAPDefinitionEventTask getEventTaskDefinitionById(String id, HAPParserEventTask eventTaskParser){
		String file = HAPFileUtility.getCronJobFolder()+id+".res";
		HAPDefinitionEventTask eventTaskDef = eventTaskParser.parseFile(file);
		return eventTaskDef;
	}
}
