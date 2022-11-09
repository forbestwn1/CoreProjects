package com.nosliw.data.core.domain.entity.expression;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

@HAPEntityWithAttribute(baseName="EXPRESSIONSUITE")
public class HAPExecutableExpressionSuite extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE;

	
	public HAPExecutableExpressionSuite() {
		super(ENTITY_TYPE);
	}
	
	@Override
	public String toString(HAPDomainEntityExecutableResourceComplex domain) {
		// TODO Auto-generated method stub
		return null;
	}


}
