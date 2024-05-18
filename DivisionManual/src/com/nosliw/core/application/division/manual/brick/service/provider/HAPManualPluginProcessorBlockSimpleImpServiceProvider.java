package com.nosliw.core.application.division.manual.brick.service.provider;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.brick.service.provider.HAPBlockServiceProvider;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimpleImp;

public class HAPManualPluginProcessorBlockSimpleImpServiceProvider extends HAPPluginProcessorBlockSimpleImp{

	public HAPManualPluginProcessorBlockSimpleImpServiceProvider() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEPROVIDER);
	}

	@Override
	public void process(HAPBrickBlockSimple blockExe, HAPManualBrickBlockSimple blockDef,
			HAPManualContextProcessBrick processContext) {
		HAPManualBlockSimpleServiceProvider serviceProviderDef = (HAPManualBlockSimpleServiceProvider)blockDef;
		HAPBlockServiceProvider serviceProviderExe = (HAPBlockServiceProvider)blockExe;
		serviceProviderExe.setServiceKey(serviceProviderDef.getServiceKey());
	}
}
