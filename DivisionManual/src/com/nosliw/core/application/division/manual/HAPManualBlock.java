package com.nosliw.core.application.division.manual;

import java.util.Set;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.common.valueport.HAPDefinitionValuePort;

public abstract class HAPManualBlock extends HAPManualBrick{

	protected HAPManualBlock (HAPIdBrickType entityType) {
		super(entityType);
	}

	public abstract Set<HAPDefinitionValuePort> getValuePorts();
}
