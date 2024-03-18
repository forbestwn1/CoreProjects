package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBrickDefinitionComplexImp;
import com.nosliw.data.core.domain.entity.test.complex.testcomplex1.HAPExecutableTestComplex1;

public class HAPPluginEntityProcessorComplexTestComplex1 extends HAPPluginProcessorBrickDefinitionComplexImp{

	public HAPPluginEntityProcessorComplexTestComplex1() {
		super(HAPEnumBrickType.TEST_COMPLEX_1_100, HAPExecutableTestComplex1.class);
	}

}
