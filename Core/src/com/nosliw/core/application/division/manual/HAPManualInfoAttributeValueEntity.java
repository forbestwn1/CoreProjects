package com.nosliw.core.application.division.manual;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualInfoAttributeValueEntity extends HAPManualInfoAttributeValueWithEntity{

	public HAPManualInfoAttributeValueEntity(HAPManualEntity entity) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_ENTITY, entity.getEntityTypeId());
		this.setEntity(entity);
	}
}
