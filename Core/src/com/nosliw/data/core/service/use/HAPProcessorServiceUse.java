package com.nosliw.data.core.service.use;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdEmbeded;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.definition.HAPDefinitionService;
import com.nosliw.data.core.service.interfacee.HAPInfoServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPUtilityServiceInterface;
import com.nosliw.data.core.structure.HAPContainerStructure;
import com.nosliw.data.core.structure.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.structure.dataassociation.HAPExecutableTask;
import com.nosliw.data.core.structure.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.structure.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.structure.dataassociation.HAPUtilityDAProcess;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;

public class HAPProcessorServiceUse {

	private static final String ENHANCECONTEXT = "enhanceContext"; 
	
	public static boolean isEnhanceContextByService(HAPDefinitionServiceUse definition) {
		boolean out = false;
		String value = (String)definition.getInfoValue(ENHANCECONTEXT);
		if(HAPBasicUtility.isStringNotEmpty(value))  out = Boolean.valueOf(value);
		return out;
	}
	
	//enhance external context according to mapping with service
	public static void enhanceContextByService(HAPDefinitionServiceUse definition, HAPStructureValueDefinition globalContext, HAPRuntimeEnvironment runtimeEnv) {
		if(HAPProcessorServiceUse.isEnhanceContextByService(definition)) {
			//process service use
			HAPServiceInterface serviceInterface = ((HAPInfoServiceInterface)HAPUtilityResource.solidateResource(definition.getInterfaceId(), runtimeEnv)).getInterface();
			
			//
			HAPProcessorDataAssociation.enhanceDataAssociationWithTaskEndPointContext(HAPUtilityServiceInterface.buildIOTaskByInterface(serviceInterface), false, definition.getDataMapping(), HAPContainerStructure.createDefault(globalContext), true, runtimeEnv);
		}
	}

	public static void normalizeServiceUse(HAPDefinitionServiceUse definition, HAPContainerAttachment attachmentContainer, HAPRuntimeEnvironment runtimeEnv) {
		if(definition.getInterfaceId()==null) {
			//if interface id does not defined, then get it from provider
			HAPAttachment providerAttachment = attachmentContainer.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE, definition.getProvider());
			//provider service id
			HAPResourceId resourceId = ((HAPAttachmentReference)providerAttachment).getReferenceId();
			//interface within provider service
			definition.setInterfaceId(new HAPResourceIdEmbeded(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, resourceId, HAPDefinitionService.CHILD_INTERFACE));
		}
	}
	
	public static HAPExecutableServiceUse process(HAPDefinitionServiceUse definition, HAPStructureValueDefinition globalContext, HAPContainerAttachment attachmentContainer, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableServiceUse out = new HAPExecutableServiceUse(definition);

		//process service use
		HAPServiceInterface serviceInterface = ((HAPInfoServiceInterface)HAPUtilityResource.solidateResource(definition.getInterfaceId(), runtimeEnv)).getInterface();

		HAPExecutableTask taskExe = HAPUtilityServiceInterface.buildExecutableTaskByInterface(serviceInterface);
		
		HAPExecutableWrapperTask serviceMappingExe = HAPProcessorDataAssociation.processDataAssociationWithTask(definition.getDataMapping(), taskExe, HAPContainerStructure.createDefault(globalContext), HAPUtilityDAProcess.withModifyInputStructureConfigureTrue(null), attachmentContainer, runtimeEnv);
		out.setServiceMapping(serviceMappingExe);
		
		//process service provider
		HAPAttachment providerAttachment = attachmentContainer.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE, definition.getProvider());
		HAPInfoServiceProvider serviceProviderInfo = HAPUtilityServiceUse.parseServiceAttachment(providerAttachment, runtimeEnv);
		HAPServiceInterface providerInterface = serviceProviderInfo.getServiceDefinition().getStaticInfo().getInterface().getInterface();
		out.setProviderId(serviceProviderInfo.getServiceDefinition().getStaticInfo().getId());
		
		if(serviceProviderInfo.getDataMapping()!=null) {
			HAPExecutableProviderToUse providerToUseExe = new HAPExecutableProviderToUse();
			
			HAPExecutableDataAssociation parmDA = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(HAPUtilityServiceUse.buildContextFromServiceParms(serviceInterface)), serviceProviderInfo.getDataMapping().getInputMapping(), HAPContainerStructure.createDefault(HAPUtilityServiceUse.buildContextFromServiceParms(providerInterface)), attachmentContainer, null, runtimeEnv);;
			providerToUseExe.setParmMapping(parmDA);
			
			for(String result : serviceProviderInfo.getDataMapping().getOutputMapping().keySet()) {
				HAPExecutableDataAssociation resultDA = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(HAPUtilityServiceUse.buildContextFromResultServiceOutputs(providerInterface, result)), serviceProviderInfo.getDataMapping().getInputMapping(), HAPContainerStructure.createDefault(HAPUtilityServiceUse.buildContextFromResultServiceOutputs(serviceInterface, result)), attachmentContainer, null, runtimeEnv);;
				providerToUseExe.addResultMapping(result, resultDA);
			}
			out.setProviderMapping(providerToUseExe);
		}
		
		return out;
	}
	
}
