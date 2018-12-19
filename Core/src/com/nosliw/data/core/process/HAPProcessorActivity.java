package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.script.context.HAPContextGroup;

public interface HAPProcessorActivity {

	/**
	 */
	HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition,
			HAPExecutableProcess processExe,
			HAPContextGroup context,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions,
			HAPManagerProcess processManager,
			HAPProcessContext processContext
	);

}
