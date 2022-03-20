package com.nosliw.data.core.domain.entity.expression;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.HAPDomainExecutableEntity;

@HAPEntityWithAttribute(baseName="EXPRESSIONSUITE")
public class HAPExecutableExpressionSuite extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE;

	
	public HAPExecutableExpressionSuite() {
	}
	
	@Override
	public String getEntityType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(HAPDomainExecutableEntity domain) {
		// TODO Auto-generated method stub
		return null;
	}


}
