package com.nosliw.data.core.activity.plugin;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPExecutableActivity;
import com.nosliw.data.core.activity.HAPExecutableResultActivity;
import com.nosliw.data.core.activity.HAPProcessorActivity;
import com.nosliw.data.core.activity.HAPUtilityActivity;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.component.attachment.HAPContextProcessor;
import com.nosliw.data.core.component.event.HAPDefinitionEvent;
import com.nosliw.data.core.component.event.HAPExecutableEvent;
import com.nosliw.data.core.component.event.HAPProcessEvent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public class HAPEventTrigueActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id,
			HAPContextProcessor processContext, 
			HAPWrapperValueStructure valueStructureWrapper,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure configure, 
			HAPProcessTracker processTracker) {
		HAPEventTrigueActivityDefinition trigueEventActDef = (HAPEventTrigueActivityDefinition)activityDefinition;
		HAPEventTrigueActivityExecutable out = new HAPEventTrigueActivityExecutable(id, trigueEventActDef);

		//get event definition
		HAPDefinitionEvent eventDef = null;
		HAPDefinitionEntityComplex complexEntity = processContext.getComplexEntity();
		if(complexEntity instanceof HAPComponent) {
			eventDef = ((HAPComponent)complexEntity).getEvent(trigueEventActDef.getEventName());
		}

		HAPExecutableEvent eventExe = HAPProcessEvent.process(eventDef, valueStructureWrapper.getValueStructure(), runtimeEnv);
		out.setEvent(eventExe);
		
		//process input
		out.setInputDataAssociation(HAPUtilityActivity.processActivityInputDataAssocation(trigueEventActDef, valueStructureWrapper.getValueStructure(), runtimeEnv));

		//process success result
		out.addResult(HAPConstantShared.ACTIVITY_RESULT_SUCCESS, new HAPExecutableResultActivity());
		
		return out;
	}
}
