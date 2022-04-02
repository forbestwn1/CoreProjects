package com.nosliw.data.core.domain.testing;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;

public class HAPDefinitionEntityTestSimple1 extends HAPDefinitionEntityInDomain{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1;

	public HAPDefinitionEntityTestSimple1() {
		super(ENTITY_TYPE);
	}
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestSimple1 out = new HAPDefinitionEntityTestSimple1();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
