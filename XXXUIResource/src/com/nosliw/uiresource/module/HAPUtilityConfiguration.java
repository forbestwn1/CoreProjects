package com.nosliw.uiresource.module;

import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.uiresource.common.HAPUtilityCommon;

public class HAPUtilityConfiguration {

	public static HAPConfigureProcessorValueStructure getContextProcessConfigurationForModule() {
		HAPConfigureProcessorValueStructure out = new HAPConfigureProcessorValueStructure();
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}

	
}
