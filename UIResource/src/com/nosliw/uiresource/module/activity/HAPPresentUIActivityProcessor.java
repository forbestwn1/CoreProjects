package com.nosliw.uiresource.module.activity;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPExecutableResultActivity;
import com.nosliw.data.core.activity.HAPProcessorActivity;
import com.nosliw.data.core.domain.entity.valuestructure.HAPValueStructureGrouped;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;

public class HAPPresentUIActivityProcessor implements HAPProcessorActivity{

	@Override
	public com.nosliw.data.core.activity.HAPExecutableActivity process(HAPDefinitionActivity activityDefinition,
			String id, com.nosliw.data.core.component.HAPContextProcessor processContext,
			HAPValueStructureGrouped valueStructureWrapper, HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure configure, HAPProcessTracker processTracker) {
		HAPPresentUIActivityDefinition activity = (HAPPresentUIActivityDefinition)activityDefinition;
		HAPPresentUIActivityExecutable out = new HAPPresentUIActivityExecutable(id, activity);
		//process success result
		out.addResult(HAPConstantShared.ACTIVITY_RESULT_SUCCESS, new HAPExecutableResultActivity());
		
		return out;
	}

}
