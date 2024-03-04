package com.nosliw.data.core.entity.division.manual;

import java.util.Set;

import com.nosliw.data.core.domain.valueport.HAPDefinitionValuePort;
import com.nosliw.data.core.entity.HAPIdEntityType;

public abstract class HAPManualEntityBlock extends HAPManualEntity{

	protected HAPManualEntityBlock (HAPIdEntityType entityType) {
		super(entityType);
	}

	public abstract Set<HAPDefinitionValuePort> getValuePorts();
}
