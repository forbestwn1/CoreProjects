package com.nosliw.data.core.domain.entity.test.complex.testcomplex1;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public class HAPDefinitionEntityTestComplex1 extends HAPDefinitionEntityInDomainComplex{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1;

	public HAPDefinitionEntityTestComplex1() {
		super(ENTITY_TYPE);
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestComplex1 out = new HAPDefinitionEntityTestComplex1();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
