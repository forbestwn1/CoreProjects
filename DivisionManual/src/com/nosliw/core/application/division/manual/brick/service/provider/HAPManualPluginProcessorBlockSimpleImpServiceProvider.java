package com.nosliw.core.application.division.manual.brick.service.provider;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockSimpleImpServiceProvider extends HAPPluginProcessorBlockSimple{

	public HAPManualPluginProcessorBlockSimpleImpServiceProvider(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.SERVICEPROVIDER_100, HAPManualBlockSimpleServiceProvider.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void process(HAPManualBrick blockExe, HAPManualDefinitionBrickBlockSimple blockDef,
			HAPManualContextProcessBrick processContext) {
		HAPManualDefinitionBlockSimpleServiceProvider serviceProviderDef = (HAPManualDefinitionBlockSimpleServiceProvider)blockDef;
		HAPManualBlockSimpleServiceProvider serviceProviderExe = (HAPManualBlockSimpleServiceProvider)blockExe;
		serviceProviderExe.setServiceKey(serviceProviderDef.getServiceKey());
	}
}
