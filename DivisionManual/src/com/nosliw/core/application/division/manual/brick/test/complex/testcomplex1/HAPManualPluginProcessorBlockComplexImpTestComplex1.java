package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockComplexImpTestComplex1 extends HAPPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockComplexImpTestComplex1(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TEST_COMPLEX_1_100, HAPManualBlockTestComplex1.class, runtimeEnv);
	}

}
