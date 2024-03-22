package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;

public class HAPManualTestComplex1 extends HAPManualBrickComplex{

	public HAPManualTestComplex1() {
		super(HAPEnumBrickType.TEST_COMPLEX_1_100);
	}

	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPManualTestComplex1 out = new HAPManualTestComplex1();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
