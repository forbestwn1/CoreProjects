package com.nosliw.uiresource.module;

import com.nosliw.common.utils.HAPFileUtility;

public class HAPUtilityModule {
	
	public static HAPDefinitionModule getUIModuleDefinitionById(String id, HAPParserModule moduleParser){
		String file = HAPFileUtility.getUIModuleFolder()+id+".res";
		HAPDefinitionModule moduleDef = moduleParser.parseFile(file);
		return moduleDef;
	}

}
