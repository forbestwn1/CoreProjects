package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPPluginProcessorEntityDefinitionComplexImp;
import com.nosliw.data.core.domain.entity.test.complex.testcomplex1.HAPExecutableTestComplex1;

public class HAPPluginEntityProcessorComplexTestComplex1 extends HAPPluginProcessorEntityDefinitionComplexImp{

	public HAPPluginEntityProcessorComplexTestComplex1() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, HAPExecutableTestComplex1.class);
	}

}
