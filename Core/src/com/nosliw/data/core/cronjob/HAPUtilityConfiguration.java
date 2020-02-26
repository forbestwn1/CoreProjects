package com.nosliw.data.core.cronjob;

import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

public class HAPUtilityConfiguration {

	public static HAPConfigureContextProcessor getContextProcessConfigurationForCronJob() {
		HAPConfigureContextProcessor out = new HAPConfigureContextProcessor();
		return out;
	}

	
}
