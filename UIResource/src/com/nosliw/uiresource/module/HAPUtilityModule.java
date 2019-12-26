package com.nosliw.uiresource.module;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.external.HAPDefinitionExternalMapping;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

public class HAPUtilityModule {
	
	public static HAPDefinitionModule getUIModuleDefinitionById(String id, HAPParserModule moduleParser){
		String file = HAPFileUtility.getUIModuleFolder()+id+".res";
		HAPDefinitionModule moduleDef = moduleParser.parseFile(file);
		return moduleDef;
	}

	public static void solveExternalMapping(HAPDefinitionModule uiModuleDef, HAPDefinitionExternalMapping parentExternalMapping) {
		uiModuleDef.getExternalMapping().merge(parentExternalMapping, HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT);
	}
	
}
