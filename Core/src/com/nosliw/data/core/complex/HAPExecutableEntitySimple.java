package com.nosliw.data.core.complex;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntitySimple extends HAPExecutableEntity{

	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";
	
	
	public HAPExecutableEntitySimple(String entityType) {
		super(entityType);
	}
	
}
