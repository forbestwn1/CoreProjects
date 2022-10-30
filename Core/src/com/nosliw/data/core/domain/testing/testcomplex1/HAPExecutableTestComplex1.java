package com.nosliw.data.core.domain.testing.testcomplex1;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;

@HAPEntityWithAttribute
public class HAPExecutableTestComplex1 extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPDefinitionEntityTestComplex1.ENTITY_TYPE;

	public HAPExecutableTestComplex1() {
		super(ENTITY_TYPE);
	}
	
}
