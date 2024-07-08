package com.nosliw.core.application.division.manual.brick.service.provider;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.service.provider.HAPBlockServiceProvider;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimpleImp;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;

public class HAPManualPluginProcessorBlockSimpleImpServiceProvider extends HAPPluginProcessorBlockSimpleImp{

	public HAPManualPluginProcessorBlockSimpleImpServiceProvider() {
		super(HAPEnumBrickType.SERVICEPROVIDER_100);
	}

	@Override
	public void process(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef,
			HAPManualContextProcessBrick processContext) {
		HAPManualBlockSimpleServiceProvider serviceProviderDef = (HAPManualBlockSimpleServiceProvider)blockDef;
		HAPBlockServiceProvider serviceProviderExe = (HAPBlockServiceProvider)blockExe;
		serviceProviderExe.setServiceKey(serviceProviderDef.getServiceKey());
	}
}
