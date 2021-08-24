package com.nosliw.data.core.task;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public interface HAPProcessorTask {

	/**
	 */
	HAPExecutableTask process(
			HAPDefinitionTask taskDefinition,
			String id,
			HAPContextProcessor processContext,
			HAPWrapperValueStructure valueStructureWrapper,
			HAPProcessTracker processTracker
	);

}
