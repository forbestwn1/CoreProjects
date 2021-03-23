package com.nosliw.data.core.service.use;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableTask;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPUtilityDAProcess;
import com.nosliw.data.core.service.interfacee.HAPInfoServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPUtilityServiceInterface;

public class HAPProcessorServiceUse {

	private static final String ENHANCECONTEXT = "enhanceContext"; 
	
	public static boolean isHnhanceContextByService(HAPDefinitionServiceUse definition) {
		boolean out = false;
		String value = (String)definition.getInfoValue(ENHANCECONTEXT);
		if(HAPBasicUtility.isStringNotEmpty(value))  out = Boolean.valueOf(value);
		return out;
	}
	
	//enhance external context according to mapping with service
	public static void enhanceContextByService(HAPDefinitionServiceUse definition, HAPContextStructure globalContext, HAPRuntimeEnvironment runtimeEnv) {
		if(HAPProcessorServiceUse.isHnhanceContextByService(definition)) {
			//process service use
			HAPServiceInterface serviceInterface = ((HAPInfoServiceInterface)HAPUtilityResource.solidateResource(definition.getInterfaceId(), runtimeEnv)).getInterface();
			
			//
			HAPProcessorDataAssociation.enhanceDataAssociationWithTaskEndPointContext(HAPUtilityServiceInterface.buildIOTaskByInterface(serviceInterface), false, definition.getDataMapping(), HAPParentContext.createDefault(globalContext), true, runtimeEnv);
		}
	}
	
	public static HAPExecutableServiceUse process(HAPDefinitionServiceUse definition, HAPContextStructure globalContext, HAPContainerAttachment attachmentContainer, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableServiceUse out = new HAPExecutableServiceUse(definition);

		//process service use
		HAPServiceInterface serviceInterface = ((HAPInfoServiceInterface)HAPUtilityResource.solidateResource(definition.getInterfaceId(), runtimeEnv)).getInterface();

		HAPExecutableTask taskExe = HAPUtilityServiceInterface.buildExecutableTaskByInterface(serviceInterface);
		
		HAPExecutableWrapperTask serviceMappingExe = HAPProcessorDataAssociation.processDataAssociationWithTask(definition.getDataMapping(), taskExe, HAPParentContext.createDefault(globalContext), HAPUtilityDAProcess.withModifyInputStructureConfigureTrue(null), runtimeEnv);
		out.setServiceMapping(serviceMappingExe);
		
		//process service provider
		HAPAttachment providerAttachment = attachmentContainer.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE, definition.getProvider());
		HAPInfoServiceProvider serviceProviderInfo = HAPUtilityServiceUse.parseServiceAttachment(providerAttachment, runtimeEnv);
		HAPServiceInterface providerInterface = serviceProviderInfo.getServiceDefinition().getStaticInfo().getInterface().getInterface();
		out.setProviderId(serviceProviderInfo.getServiceDefinition().getStaticInfo().getId());
		
		if(serviceProviderInfo.getDataMapping()!=null) {
			HAPExecutableProviderToUse providerToUseExe = new HAPExecutableProviderToUse();
			
			HAPExecutableDataAssociation parmDA = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(HAPUtilityServiceUse.buildContextFromServiceParms(serviceInterface)), serviceProviderInfo.getDataMapping().getInputMapping(), HAPParentContext.createDefault(HAPUtilityServiceUse.buildContextFromServiceParms(providerInterface)), null, runtimeEnv);;
			providerToUseExe.setParmMapping(parmDA);
			
			for(String result : serviceProviderInfo.getDataMapping().getOutputMapping().keySet()) {
				HAPExecutableDataAssociation resultDA = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(HAPUtilityServiceUse.buildContextFromResultServiceOutputs(providerInterface, result)), serviceProviderInfo.getDataMapping().getInputMapping(), HAPParentContext.createDefault(HAPUtilityServiceUse.buildContextFromResultServiceOutputs(serviceInterface, result)), null, runtimeEnv);;
				providerToUseExe.addResultMapping(result, resultDA);
			}
			out.setProviderMapping(providerToUseExe);
		}
		
		return out;
	}
	
//	public static HAPExecutableServiceUse process(
//			HAPDefinitionServiceUse definition,
//			HAPServiceInterface providerInterface,
//			HAPContextStructure globalContext, 
//			HAPConfigureContextProcessor configure, 
//			HAPRuntimeEnvironment runtimeEnv) {
//		HAPExecutableServiceUse out = new HAPExecutableServiceUse(definition);
//		
//		HAPExecutableTask taskExe = new HAPExecutableTask() {
//			@Override
//			public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {		return HAPResourceDataFactory.createJSValueResourceData("");	}
//
//			@Override
//			public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {		return null;	}
//
//			@Override
//			public HAPParentContext getInContext() {
//				return HAPParentContext.createDefault(HAPUtilityServiceUse.buildContextFromServiceParms(providerInterface));
//			}
//
//			@Override
//			public Map<String, HAPParentContext> getOutResultContext() {
//				Map<String, HAPParentContext> out = new LinkedHashMap<String, HAPParentContext>();
//				Map<String, HAPContext> resultsContext = HAPUtilityServiceUse.buildContextFromResultServiceOutputs(providerInterface);
//				for(String resultName : resultsContext.keySet()) {
//					out.put(resultName, HAPParentContext.createDefault(resultsContext.get(resultName)));
//				}
//				return out;
//			}
//
//			@Override
//			public String toStringValue(HAPSerializationFormat format) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public boolean buildObject(Object value, HAPSerializationFormat format) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		};
//		HAPExecutableWrapperTask serviceMappingExe = HAPProcessorDataAssociation.processDataAssociationWithTask(definition.getServiceMapping(), taskExe, HAPParentContext.createDefault(globalContext), null, runtimeEnv);
//		out.setServiceMapping(serviceMappingExe);
//		
//		return out;
//	}
	
}
