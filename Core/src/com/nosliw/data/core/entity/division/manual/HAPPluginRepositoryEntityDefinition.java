package com.nosliw.data.core.entity.division.manual;

import com.nosliw.data.core.entity.HAPIdEntity;
import com.nosliw.data.core.entity.HAPIdEntityType;

public interface HAPPluginRepositoryEntityDefinition {

	HAPIdEntityType getEntityType();
	
	HAPManualEntity retrieveEntityDefinition(HAPIdEntity entityId);
	
}
