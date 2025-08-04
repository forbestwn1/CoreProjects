package com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1;

import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockComplexImpTestComplex1 extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockComplexImpTestComplex1(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.TEST_COMPLEX_1_100, HAPManualBlockTestComplex1.class, runtimeEnv, manualBrickMan);
	}

}
