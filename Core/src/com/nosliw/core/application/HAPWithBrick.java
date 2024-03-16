package com.nosliw.core.application;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithBrick {

	@HAPAttribute
	public static final String ENTITY = "entity";

	@HAPAttribute
	public static final String ENTITYTYPEID = "entityTypeId";

	HAPBrick getBrick();
	
	HAPIdBrickType getBrickTypeId();
	
}
