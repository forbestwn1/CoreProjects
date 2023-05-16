package com.nosliw.data.core.domain.entity.service.provider;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPPluginEntityProcessorSimpleImp;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;

public class HAPPluginSimpleEntityProcessorServiceProvider extends HAPPluginEntityProcessorSimpleImp{

	public HAPPluginSimpleEntityProcessorServiceProvider() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEPROVIDER, HAPExecutableEntityInDomainServiceProvider.class);
	}

	@Override
	protected void process(HAPExecutableEntity entityExe, HAPDefinitionEntityInDomain entityDef, HAPContextProcessor processContext) {
		HAPDefinitionEntityInDomainServiceProvider serviceProviderDef = (HAPDefinitionEntityInDomainServiceProvider)entityDef;
		HAPExecutableEntityInDomainServiceProvider serviceProviderExe = (HAPExecutableEntityInDomainServiceProvider)entityExe;
		
		String serviceId = serviceProviderDef.getServiceKey().getServiceId();
		HAPServiceInterface serviceInterface = processContext.getRuntimeEnvironment().getServiceManager().getServiceDefinitionManager().getDefinition(serviceId).getStaticInfo().getInterface().getInterface();
		serviceProviderExe.setServiceInterface(serviceInterface);
	}
}
