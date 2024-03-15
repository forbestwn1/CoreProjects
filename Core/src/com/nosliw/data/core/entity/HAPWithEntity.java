package com.nosliw.data.core.entity;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithEntity {

	@HAPAttribute
	public static final String ENTITY = "entity";

	@HAPAttribute
	public static final String ENTITYTYPEID = "entityTypeId";

	HAPEntityExecutable getEntity();
	
	HAPIdEntityType getEntityTypeId();
	
}
