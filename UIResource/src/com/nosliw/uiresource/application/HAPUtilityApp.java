package com.nosliw.uiresource.application;

import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.uiresource.common.HAPUtilityCommon;

public class HAPUtilityApp {

	public static final String ENTRY_DEFAULT = "defaultEntry";

//	public static HAPDefinitionApp getAppDefinitionById(String id, HAPParseMiniApp miniAppParser){
//		String file = HAPFileUtility.getMiniAppFolder()+id+".res";
//		HAPDefinitionApp miniAppDef = miniAppParser.parseFile(file);
//		return miniAppDef;
//	}
	
	public static HAPConfigureProcessorStructure getContextProcessConfigurationForApp() {
		HAPConfigureProcessorStructure out = new HAPConfigureProcessorStructure();
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}

}
