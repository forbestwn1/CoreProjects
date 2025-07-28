package com.nosliw.data.core.task;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickWrapperValueStructure;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;

public interface HAPProcessorTask {

	/**
	 */
	HAPExecutableTask process(
			HAPDefinitionTask taskDefinition,
			String id,
			HAPContextProcessor processContext,
			HAPManualBrickWrapperValueStructure valueStructureWrapper,
			HAPProcessTracker processTracker
	);

}
