package com.nosliw.uiresource.application;

import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.uiresource.common.HAPUtilityCommon;

public class HAPUtilityApp {

	public static final String ENTRY_DEFAULT = "defaultEntry";

//	public static HAPDefinitionApp getAppDefinitionById(String id, HAPParseMiniApp miniAppParser){
//		String file = HAPFileUtility.getMiniAppFolder()+id+".res";
//		HAPDefinitionApp miniAppDef = miniAppParser.parseFile(file);
//		return miniAppDef;
//	}
	
	public static HAPConfigureProcessorValueStructure getContextProcessConfigurationForApp() {
		HAPConfigureProcessorValueStructure out = new HAPConfigureProcessorValueStructure();
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}

}
