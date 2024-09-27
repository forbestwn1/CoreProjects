package com.nosliw.core.application.division.manual.definition;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualDefinitionWrapperValueBrick extends HAPManualDefinitionWrapperValueWithBrick{

	public HAPManualDefinitionWrapperValueBrick(HAPManualDefinitionBrick brick) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK, brick.getBrickTypeId());
		this.setBrick(brick);
	}
}
