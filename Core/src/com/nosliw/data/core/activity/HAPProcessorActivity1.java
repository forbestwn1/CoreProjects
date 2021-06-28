package com.nosliw.data.core.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public interface HAPProcessorActivity1 {

	/**
	 */
	HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition,
			String id,
			HAPContextProcessAttachmentReferenceActivity processContext,
			HAPWrapperValueStructure valueStructureWrapper,
			Map<String, HAPExecutableDataAssociation> results,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker
	);

}
