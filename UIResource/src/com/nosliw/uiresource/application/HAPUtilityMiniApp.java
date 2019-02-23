package com.nosliw.uiresource.application;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

public class HAPUtilityMiniApp {

	public static final String ENTRY_DEFAULT = "defaultEntry";

	public static HAPDefinitionMiniApp getMiniAppDefinitionById(String id, HAPParseMiniApp miniAppParser){
		String file = HAPFileUtility.getMiniAppFolder()+id+".res";
		HAPDefinitionMiniApp miniAppDef = miniAppParser.parseFile(file);
		return miniAppDef;
	}
	
	public static HAPConfigureContextProcessor getContextProcessConfigurationForMiniApp() {
		HAPConfigureContextProcessor out = new HAPConfigureContextProcessor();
		return out;
	}
}
