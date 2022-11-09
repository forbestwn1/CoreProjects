package com.nosliw.data.core.domain.entity;

import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntitySimple extends HAPExecutableEntity{

	public HAPExecutableEntitySimple(String entityType) {
		super(entityType);
	}
	
}
