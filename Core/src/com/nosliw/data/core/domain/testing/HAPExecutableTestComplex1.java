package com.nosliw.data.core.domain.testing;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.HAPDomainExecutableEntity;

@HAPEntityWithAttribute(baseName="EXPRESSIONSUITE")
public class HAPExecutableTestComplex1 extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPDefinitionEntityTestComplex1.ENTITY_TYPE;

	
	public HAPExecutableTestComplex1() {
		super(ENTITY_TYPE);
	}
	
	@Override
	public String toString(HAPDomainExecutableEntity domain) {
		// TODO Auto-generated method stub
		return null;
	}


}
