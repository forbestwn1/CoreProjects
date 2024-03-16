package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;

public interface HAPManualWithEntity {

	//entity definition
	public static final String ENTITY = "entity";

	public static final String ENTITYTYPEID = "entityTypeId";

	HAPManualEntity getEntity();
	
	HAPIdBrickType getEntityTypeId();
	
}
