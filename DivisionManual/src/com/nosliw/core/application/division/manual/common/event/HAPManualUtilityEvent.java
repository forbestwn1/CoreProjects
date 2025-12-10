package com.nosliw.core.application.division.manual.common.event;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.common.event.HAPEventDefinition;
import com.nosliw.core.application.common.event.HAPEventInfoHandlerTask;
import com.nosliw.core.application.common.event.HAPEventUtilityValuePort;
import com.nosliw.core.application.common.task.HAPUtilityTask;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;

public class HAPManualUtilityEvent {

	public static void buildValuePortForEventHandlerTask(HAPEventDefinition eventDef, HAPEventInfoHandlerTask handlerTask, String rootNameIfNotProvide, HAPBundle bundle) {
		HAPManualBrick childBrick = (HAPManualBrick)HAPUtilityTask.getDescdentBrickLocalTask(bundle, new HAPPath(handlerTask.getTaskBrickId().getIdPath()), rootNameIfNotProvide);
		if(childBrick!=null) {
			HAPEventUtilityValuePort.buildValuePortGroupForEventHandler(Pair.of(childBrick.getOtherInternalValuePortContainer(), childBrick.getOtherExternalValuePortContainer()), eventDef, bundle.getValueStructureDomain());
		}
	}

}
