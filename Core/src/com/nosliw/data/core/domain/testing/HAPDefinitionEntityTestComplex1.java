package com.nosliw.data.core.domain.testing;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomain;

public class HAPDefinitionEntityTestComplex1 extends HAPDefinitionEntityInDomain{

	public HAPDefinitionEntityTestComplex1() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX1);
	}
	
	@Override
	public HAPEntityOrReference getChild(String childName) {	return this.getChild(childName);	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestComplex1 out = new HAPDefinitionEntityTestComplex1();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
