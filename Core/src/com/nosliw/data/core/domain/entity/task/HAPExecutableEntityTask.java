package com.nosliw.data.core.domain.entity.task;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

@HAPEntityWithAttribute
public class HAPExecutableEntityTask extends HAPExecutableEntityComplex{

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String IMPLEMENTATION = "implementation";

	public void setImpEntityType(String type) {   this.setAttributeValueObject(HAPExecutableEntityTask.TYPE, type);       }
	
}
