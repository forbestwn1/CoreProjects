package com.nosliw.core.application.entity.brickfacade;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPFacadeBrickTypeSimple extends HAPFacadeBrickType{

	public HAPFacadeBrickTypeSimple(String name, String description) {
		super(HAPConstantShared.BRICKFACADE_TYPE_SIMPLE);
		this.setName(name);
		this.setDescription(description);
	}

	
}
