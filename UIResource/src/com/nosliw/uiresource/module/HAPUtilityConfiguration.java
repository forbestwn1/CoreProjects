package com.nosliw.uiresource.module;

import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.uiresource.common.HAPUtilityCommon;

public class HAPUtilityConfiguration {

	public static HAPConfigureContextProcessor getContextProcessConfigurationForModule() {
		HAPConfigureContextProcessor out = new HAPConfigureContextProcessor();
		out.inheritanceExcludedInfo = HAPUtilityCommon.getDefaultInheritanceExcludedInfo();
		return out;
	}

	
}
