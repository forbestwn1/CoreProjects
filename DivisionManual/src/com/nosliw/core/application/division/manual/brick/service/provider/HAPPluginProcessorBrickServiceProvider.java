package com.nosliw.core.application.division.manual.brick.service.provider;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.brick.service.interfacee.HAPBrickServiceInterface1;
import com.nosliw.core.application.brick.service.provider.HAPBrickServiceProvider;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimpleImp;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public class HAPPluginProcessorBrickServiceProvider extends HAPPluginProcessorBlockSimpleImp{

	public HAPPluginProcessorBrickServiceProvider() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEPROVIDER, HAPBrickServiceProvider.class);
	}

	@Override
	protected void process(HAPExecutableEntity entityExe, HAPManualBrick entityDef, HAPContextProcessor processContext) {
		HAPDefinitionEntityInDomainServiceProvider serviceProviderDef = (HAPDefinitionEntityInDomainServiceProvider)entityDef;
		HAPBrickServiceProvider serviceProviderExe = (HAPBrickServiceProvider)entityExe;
		
		String serviceId = serviceProviderDef.getServiceKey().getServiceId();
		serviceProviderExe.setServiceId(serviceId);
		HAPBrickServiceInterface1 serviceInterface = processContext.getRuntimeEnvironment().getServiceManager().getServiceDefinitionManager().getServiceInfo(serviceId).getStaticInfo().getInterface().getInterface();
		serviceProviderExe.setInteractive(serviceInterface);
	}
}
