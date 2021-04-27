package com.nosliw.uiresource.module;

import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.uiresource.common.HAPUtilityCommon;

public class HAPUtilityConfiguration {

	public static HAPConfigureProcessorStructure getContextProcessConfigurationForModule() {
		HAPConfigureProcessorStructure out = new HAPConfigureProcessorStructure();
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}

	
}
