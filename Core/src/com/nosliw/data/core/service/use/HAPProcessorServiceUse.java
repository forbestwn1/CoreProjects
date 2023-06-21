package com.nosliw.data.core.service.use;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableTask;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.dataassociation.HAPUtilityDAProcess;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.attachment1.HAPAttachmentReference;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdEmbeded;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.definition.HAPDefinitionService;
import com.nosliw.data.core.service.interfacee.HAPInfoServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPUtilityServiceInterface;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;

public class HAPProcessorServiceUse {

	private static final String ENHANCECONTEXT = "enhanceContext"; 
	
	public static boolean isEnhanceContextByService(HAPDefinitionServiceUse definition) {
		boolean out = false;
		String value = (String)definition.getInfoValue(ENHANCECONTEXT);
		if(HAPUtilityBasic.isStringNotEmpty(value))  out = Boolean.valueOf(value);
		return out;
	}
	
	//enhance external value structure according to mapping with service
	public static void enhanceValueStructureByService(HAPDefinitionServiceUse definition, HAPValueStructure globalValueStructure, HAPRuntimeEnvironment runtimeEnv) {
		if(HAPProcessorServiceUse.isEnhanceContextByService(definition)) {
			//process service use
			HAPServiceInterface serviceInterface = ((HAPInfoServiceInterface)HAPUtilityResource.solidateResource(definition.getInterfaceId(), runtimeEnv)).getInterface();
			
			//
			HAPProcessorDataAssociation.enhanceDataAssociationWithTaskEndPointValueStructure(HAPUtilityServiceInterface.buildIOTaskByInterface(serviceInterface), false, definition.getDataAssociations(), HAPContainerStructure.createDefault(globalValueStructure), true, runtimeEnv);
		}
	}

	public static void normalizeServiceUse(HAPDefinitionServiceUse definition, HAPDefinitionEntityContainerAttachment attachmentContainer, HAPRuntimeEnvironment runtimeEnv) {
		if(definition.getInterfaceId()==null) {
			//if interface id does not defined, then get it from provider
			HAPAttachment providerAttachment = attachmentContainer.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE, definition.getProvider());
			//provider service id
			HAPResourceId resourceId = ((HAPAttachmentReference)providerAttachment).getReferenceId();
			//interface within provider service
			definition.setInterfaceId(new HAPResourceIdEmbeded(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, resourceId, HAPDefinitionService.CHILD_INTERFACE));
		}
	}
	
	public static HAPExecutableServiceUse process(HAPDefinitionServiceUse definition, HAPValueStructure globalValueStructure, HAPDefinitionEntityContainerAttachment attachmentContainer, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableServiceUse out = new HAPExecutableServiceUse(definition);

		//process service use
		HAPServiceInterface serviceInterface = ((HAPInfoServiceInterface)HAPUtilityResource.solidateResource(definition.getInterfaceId(), runtimeEnv)).getInterface();

		HAPExecutableTask taskExe = HAPUtilityServiceInterface.buildExecutableTaskByInterface(serviceInterface);
		
		HAPExecutableWrapperTask serviceMappingExe = HAPProcessorDataAssociation.processDataAssociationWithTask(definition.getDataAssociations(), taskExe, HAPContainerStructure.createDefault(globalValueStructure), HAPUtilityDAProcess.withModifyInputStructureConfigureTrue(null), attachmentContainer, runtimeEnv);
		out.setServiceMapping(serviceMappingExe);
		
		//process service provider
		HAPAttachment providerAttachment = attachmentContainer.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE, definition.getProvider());
		HAPInfoServiceProvider serviceProviderInfo = HAPUtilityServiceUse.parseServiceAttachment(providerAttachment, runtimeEnv);
		HAPServiceInterface providerInterface = serviceProviderInfo.getServiceDefinition().getStaticInfo().getInterface().getInterface();
		out.setProviderId(serviceProviderInfo.getServiceDefinition().getStaticInfo().getId());
		
		if(serviceProviderInfo.getDataMapping()!=null) {
			HAPExecutableProviderToUse providerToUseExe = new HAPExecutableProviderToUse();
			
			HAPExecutableDataAssociation parmDA = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(HAPUtilityServiceUse.buildValueStructureFromServiceParms(serviceInterface)), serviceProviderInfo.getDataMapping().getInDataAssociation(), HAPContainerStructure.createDefault(HAPUtilityServiceUse.buildValueStructureFromServiceParms(providerInterface)), null, runtimeEnv);;
			providerToUseExe.setParmMapping(parmDA);
			
			for(String result : serviceProviderInfo.getDataMapping().getOutDataAssociations().keySet()) {
				HAPExecutableDataAssociation resultDA = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(HAPUtilityServiceUse.buildValueStructureFromResultServiceOutputs(providerInterface, result)), serviceProviderInfo.getDataMapping().getInDataAssociation(), HAPContainerStructure.createDefault(HAPUtilityServiceUse.buildValueStructureFromResultServiceOutputs(serviceInterface, result)), null, runtimeEnv);;
				providerToUseExe.addResultMapping(result, resultDA);
			}
			out.setProviderMapping(providerToUseExe);
		}
		
		return out;
	}
	
}
