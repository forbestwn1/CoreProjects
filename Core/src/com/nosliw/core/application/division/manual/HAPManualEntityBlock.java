package com.nosliw.core.application.division.manual;

import java.util.Set;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.data.core.domain.valueport.HAPDefinitionValuePort;

public abstract class HAPManualEntityBlock extends HAPManualEntity{

	protected HAPManualEntityBlock (HAPIdBrickType entityType) {
		super(entityType);
	}

	public abstract Set<HAPDefinitionValuePort> getValuePorts();
}
