package com.nosliw.data.core.domain.testing.testsimple1;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomainSimple;

public class HAPDefinitionEntityTestSimple1 extends HAPDefinitionEntityInDomainSimple{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1;

	public HAPDefinitionEntityTestSimple1() {
		super(ENTITY_TYPE);
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestSimple1 out = new HAPDefinitionEntityTestSimple1();
		out.cloneToDefinitionEntityInDomain(out);
		return out;
	}
	
}
