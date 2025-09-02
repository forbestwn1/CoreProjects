package com.nosliw.core.application.bricktypefacade;

import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPFacadeBrickTypeSingle extends HAPEntityInfoImp implements HAPFacadeBrickType{

	public HAPFacadeBrickTypeSingle(String name, String description) {
		super(name, description);
	}

	@Override
	public String getType() {
		return HAPFacadeBrickType.TYPE_SIMPLE;
	}
	
}
