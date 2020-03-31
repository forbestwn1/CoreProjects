package com.nosliw.data.core.event;

import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityEventTask {

	public static HAPDefinitionEventTask getEventTaskDefinitionById(String id, HAPParserEventTask eventTaskParser){
		String file = HAPSystemFolderUtility.getCronJobFolder()+id+".res";
		HAPDefinitionEventTask eventTaskDef = eventTaskParser.parseFile(file);
		return eventTaskDef;
	}
}
