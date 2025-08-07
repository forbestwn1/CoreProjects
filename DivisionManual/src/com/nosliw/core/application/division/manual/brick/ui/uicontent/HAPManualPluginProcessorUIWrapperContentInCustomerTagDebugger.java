package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorUIWrapperContentInCustomerTagDebugger extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorUIWrapperContentInCustomerTagDebugger(
			HAPRuntimeEnvironment runtimeEnv,
			HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.UIWRAPPERCONTENTCUSTOMERTAGDEBUGGER_100, HAPManualBlockComplexUIWrapperContentInCustomerTagDebugger.class, runtimeEnv, manualBrickMan);
	}

}
