package com.nosliw.data.core.process;

import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

public class HAPUtilityConfigure {

	public static HAPConfigureContextProcessor getContextProcessConfigurationForProcess() {
		HAPConfigureContextProcessor out = new HAPConfigureContextProcessor();
		return out;
	}

	public static HAPConfigureContextProcessor getContextProcessConfigurationForActivity() {
		HAPConfigureContextProcessor out = new HAPConfigureContextProcessor();
		return out;
	}

}
