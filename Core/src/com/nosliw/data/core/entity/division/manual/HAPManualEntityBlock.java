package com.nosliw.data.core.entity.division.manual;

import java.util.Set;

import com.nosliw.data.core.domain.valueport.HAPDefinitionValuePort;

public abstract class HAPManualEntityBlock extends HAPManualEntity{

	public HAPManualEntityBlock () {}

	protected HAPManualEntityBlock (String entityType) {
		super(entityType);
	}

	public abstract Set<HAPDefinitionValuePort> getValuePorts();
}
