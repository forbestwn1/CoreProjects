package com.nosliw.uiresource.application;

import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

public class HAPUtilityApp {

	public static final String ENTRY_DEFAULT = "defaultEntry";

//	public static HAPDefinitionApp getAppDefinitionById(String id, HAPParseMiniApp miniAppParser){
//		String file = HAPFileUtility.getMiniAppFolder()+id+".res";
//		HAPDefinitionApp miniAppDef = miniAppParser.parseFile(file);
//		return miniAppDef;
//	}
	
	public static HAPConfigureContextProcessor getContextProcessConfigurationForApp() {
		HAPConfigureContextProcessor out = new HAPConfigureContextProcessor();
		return out;
	}

}
