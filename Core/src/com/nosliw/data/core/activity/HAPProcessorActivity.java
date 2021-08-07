package com.nosliw.data.core.activity;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.attachment.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public interface HAPProcessorActivity {

	/**
	 */
	HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition,
			String id,
			HAPContextProcessor processContext,
			HAPWrapperValueStructure valueStructureWrapper,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker
	);

}
