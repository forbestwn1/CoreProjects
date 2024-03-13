package com.nosliw.data.core.entity.division.manual;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualInfoAttributeValueEntity extends HAPManualInfoAttributeValueWithEntity{

	public HAPManualInfoAttributeValueEntity(HAPManualEntity entity) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_ENTITY, entity.getEntityTypeId());
		this.setEntity(entity);
	}
}
