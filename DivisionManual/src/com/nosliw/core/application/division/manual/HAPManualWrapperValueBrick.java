package com.nosliw.core.application.division.manual;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualWrapperValueBrick extends HAPManualWrapperValueWithBrick{

	public HAPManualWrapperValueBrick(HAPManualBrick entity) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK, entity.getBrickTypeId());
		this.setBrick(entity);
	}
}
