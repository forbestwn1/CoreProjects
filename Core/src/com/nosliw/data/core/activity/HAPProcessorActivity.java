package com.nosliw.data.core.activity;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.valuestructure.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPWrapperValueStructureDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public interface HAPProcessorActivity {

	/**
	 */
	HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition,
			String id,
			HAPContextProcessor processContext,
			HAPWrapperValueStructureDefinition valueStructureWrapper,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorValueStructure configure, 
			HAPProcessTracker processTracker
	);

}
