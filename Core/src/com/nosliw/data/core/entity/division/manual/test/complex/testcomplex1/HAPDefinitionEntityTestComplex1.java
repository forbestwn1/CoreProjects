package com.nosliw.data.core.entity.division.manual.test.complex.testcomplex1;

import com.nosliw.data.core.entity.HAPEnumEntityType;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;

public class HAPDefinitionEntityTestComplex1 extends HAPManualEntityComplex{

	public HAPDefinitionEntityTestComplex1() {
		super(HAPEnumEntityType.TEST_COMPLEX_SCRIPT_100);
	}

	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestComplex1 out = new HAPDefinitionEntityTestComplex1();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
