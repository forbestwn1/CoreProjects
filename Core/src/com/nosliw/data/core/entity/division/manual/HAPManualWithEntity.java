package com.nosliw.data.core.entity.division.manual;

import com.nosliw.data.core.entity.HAPIdEntityType;

public interface HAPManualWithEntity {

	//entity definition
	public static final String ENTITY = "entity";

	public static final String ENTITYTYPEID = "entityTypeId";

	HAPManualEntity getEntity();
	
	HAPIdEntityType getEntityTypeId();
	
}
