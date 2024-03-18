package com.nosliw.data.core.domain.entity.service.provider;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBrickDefinitionSimpleImp;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;

public class HAPPluginSimpleEntityProcessorServiceProvider extends HAPPluginProcessorBrickDefinitionSimpleImp{

	public HAPPluginSimpleEntityProcessorServiceProvider() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEPROVIDER, HAPExecutableEntityInDomainServiceProvider.class);
	}

	@Override
	protected void process(HAPExecutableEntity entityExe, HAPManualBrick entityDef, HAPContextProcessor processContext) {
		HAPDefinitionEntityInDomainServiceProvider serviceProviderDef = (HAPDefinitionEntityInDomainServiceProvider)entityDef;
		HAPExecutableEntityInDomainServiceProvider serviceProviderExe = (HAPExecutableEntityInDomainServiceProvider)entityExe;
		
		String serviceId = serviceProviderDef.getServiceKey().getServiceId();
		serviceProviderExe.setServiceId(serviceId);
		HAPServiceInterface serviceInterface = processContext.getRuntimeEnvironment().getServiceManager().getServiceDefinitionManager().getDefinition(serviceId).getStaticInfo().getInterface().getInterface();
		serviceProviderExe.setInteractive(serviceInterface);
	}
}
