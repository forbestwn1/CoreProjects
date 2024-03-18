package com.nosliw.core.application.division.manual;

import java.util.Set;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.data.core.domain.valueport.HAPDefinitionValuePort;

public abstract class HAPManualBrickBlock extends HAPManualBrick{

	protected HAPManualBrickBlock (HAPIdBrickType entityType) {
		super(entityType);
	}

	public abstract Set<HAPDefinitionValuePort> getValuePorts();
}
