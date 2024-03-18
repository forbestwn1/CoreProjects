package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;

public class HAPDefinitionEntityTestComplex1 extends HAPManualBrickComplex{

	public HAPDefinitionEntityTestComplex1() {
		super(HAPEnumBrickType.TEST_COMPLEX_1_100);
	}

	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityTestComplex1 out = new HAPDefinitionEntityTestComplex1();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
