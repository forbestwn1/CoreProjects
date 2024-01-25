package com.nosliw.data.core.domain.valueport;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public class HAPUtilityValuePort {

	public static HAPValuePort getValuePort(HAPIdValuePort valuePortId, HAPContextProcessor processContext) {
		HAPExecutableEntity entityExe = processContext.getCurrentBundle().getExecutableEntityById(valuePortId.getEntityId());
		return entityExe.getValuePorts().getValuePort(valuePortId);
	}

	public static HAPIdValuePort getDefaultValuePortIdInEntity(HAPExecutableEntity entityExe) {
		return entityExe.getValuePorts().getValuePort(null).getValuePortId();
	}
	
}
