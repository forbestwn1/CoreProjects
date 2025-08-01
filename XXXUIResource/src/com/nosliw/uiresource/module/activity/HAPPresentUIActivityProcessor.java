package com.nosliw.uiresource.module.activity;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.xxx.application1.division.manual.brick.valuestructure.HAPManualBrickWrapperValueStructure;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPExecutableResultActivity;
import com.nosliw.data.core.activity.HAPProcessorActivity;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPresentUIActivityProcessor implements HAPProcessorActivity{

	@Override
	public com.nosliw.data.core.activity.HAPExecutableActivity process(HAPDefinitionActivity activityDefinition,
			String id, com.nosliw.data.core.domain.entity.HAPContextProcessor processContext,
			HAPManualBrickWrapperValueStructure valueStructureWrapper, HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorValueStructure configure, HAPProcessTracker processTracker) {
		HAPPresentUIActivityDefinition activity = (HAPPresentUIActivityDefinition)activityDefinition;
		HAPPresentUIActivityExecutable out = new HAPPresentUIActivityExecutable(id, activity);
		//process success result
		out.addResult(HAPConstantShared.ACTIVITY_RESULT_SUCCESS, new HAPExecutableResultActivity());
		
		return out;
	}

}
