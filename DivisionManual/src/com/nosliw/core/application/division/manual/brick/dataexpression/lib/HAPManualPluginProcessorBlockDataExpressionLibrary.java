package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockDataExpressionLibrary extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockDataExpressionLibrary(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100, HAPManualBlockDataExpressionLibrary.class, runtimeEnv, manualBrickMan);
	}

}
