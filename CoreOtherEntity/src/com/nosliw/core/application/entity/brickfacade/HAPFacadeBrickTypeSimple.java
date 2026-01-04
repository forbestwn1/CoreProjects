package com.nosliw.core.application.entity.brickfacade;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPFacadeBrickTypeSimple extends HAPEntityInfoImp implements HAPFacadeBrickType{

	public HAPFacadeBrickTypeSimple(String name, String description) {
		super(name, description);
	}

	@Override
	public String getType() {
		return HAPConstantShared.BRICKFACADE_TYPE_SIMPLE;
	}
	
}
