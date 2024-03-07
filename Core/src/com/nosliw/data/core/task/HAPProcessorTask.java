package com.nosliw.data.core.task;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityWrapperValueStructure;

public interface HAPProcessorTask {

	/**
	 */
	HAPExecutableTask process(
			HAPDefinitionTask taskDefinition,
			String id,
			HAPContextProcessor processContext,
			HAPDefinitionEntityWrapperValueStructure valueStructureWrapper,
			HAPProcessTracker processTracker
	);

}
