package com.nosliw.data.core.domain.valueport;

import com.nosliw.data.core.component.HAPContextProcessor;

public class HAPUtilityValuePort {

	public static HAPValuePort getValuePort(HAPIdValuePort valuePortId, HAPContextProcessor processContext) {
		return processContext.getCurrentBundle().getExecutableEntityById(valuePortId.getEntityId()).getValuePort(valuePortId.getType(), valuePortId.getValuePortName());
	}
	
}
