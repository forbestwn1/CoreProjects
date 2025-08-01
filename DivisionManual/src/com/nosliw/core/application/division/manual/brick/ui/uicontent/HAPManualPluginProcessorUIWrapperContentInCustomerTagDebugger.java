package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorUIWrapperContentInCustomerTagDebugger extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorUIWrapperContentInCustomerTagDebugger(
			HAPRuntimeEnvironment runtimeEnv,
			HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.UIWRAPPERCONTENTCUSTOMERTAGDEBUGGER_100, HAPManualBlockComplexUIWrapperContentInCustomerTagDebugger.class, runtimeEnv, manualBrickMan);
	}

}
