package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.component.attachment.HAPAttachmentUtility;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdFactory;
import com.nosliw.data.core.runtime.HAPExecutableImpComponent;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.data.HAPContextDataFlat;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceUse;

public class HAPUtilityComponent {

	public static HAPContextDataFlat getTestDataFromAttachment(HAPWithAttachment withAttachment, String name) {
		return HAPAttachmentUtility.getContextDataFromAttachment(withAttachment.getAttachmentContainer(), HAPConstant.RUNTIME_RESOURCE_TYPE_TESTDATA, name);
	}
	
	public static void processComponentExecutable(
			HAPExecutableImpComponent componentExe,
			HAPContextGroup parentContext,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPConfigureContextProcessor contextProcessConfg,
			HAPManagerActivityPlugin activityPluginMan
			) {
		HAPComponent component = componentExe.getDefinition();
		
		//service providers
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(component.getAttachmentContainer(), null, contextProcessRequirement.serviceDefinitionManager); 
		componentExe.setServiceProviders(allServiceProviders);
		
		//process context 
		componentExe.setContextStructure(HAPProcessorContext.process(component.getContextStructure(), HAPParentContext.createDefault(parentContext==null?new HAPContextGroup():parentContext), contextProcessConfg, contextProcessRequirement));
		
		//process process suite
		HAPDefinitionProcessSuite processSuite = HAPUtilityComponent.getProcessSuite(component, activityPluginMan).cloneProcessSuiteDefinition(); 
		processSuite.setContextStructure(componentExe.getContextStructure());   //kkk
		componentExe.setProcessSuite(processSuite);
	}
	
	public static HAPDefinitionProcessSuite getProcessSuite(HAPComponent component, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionProcessSuite out = new HAPDefinitionProcessSuite();
		if(component instanceof HAPComponentImp) {
			component.cloneToComplexResourceDefinition(out);
			Map<String, HAPAttachment> processAtts = component.getAttachmentContainer().getAttachmentByType(HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS);
			
			for(String name : processAtts.keySet()) {
				HAPAttachment attachment = processAtts.get(name);
				out.addElement(attachment.getName(), HAPUtilityProcess.getProcessDefinitionElementFromAttachment(attachment, activityPluginMan));
			}
		}
		else if(component instanceof HAPComponentContainerElement) {
			out = getProcessSuite(((HAPComponentContainerElement)component).getElement(), activityPluginMan);
		}
		return out;
	}

//	public static HAPDefinitionProcessSuite getProcessSuite(HAPComponent component, HAPManagerActivityPlugin activityPluginMan) {
//		HAPDefinitionProcessSuite out = component.getProcessSuite();
//		if(out==null) {
//			out = new HAPDefinitionProcessSuite();
//			if(component instanceof HAPComponentImp) {
//				component.cloneToComplexEntity(out);
//				Map<String, HAPAttachment> processAtts = component.getAttachmentContainer().getAttachmentByType(HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS);
//				
//				for(String name : processAtts.keySet()) {
//					HAPAttachment attachment = processAtts.get(name);
//					out.addProcess(attachment.getName(), HAPUtilityProcess.getProcessDefinitionElementFromAttachment(attachment, activityPluginMan));
//				}
//			}
//			else if(component instanceof HAPComponentContainerElement) {
//				out = ((HAPComponentContainerElement)component).getElement().getProcessSuite();
//			}
//			
//			component.setProcessSuite(out);
//		}
//		return out;
//	}
	
	public static HAPContextStructure processElementComponentContext(HAPComponentContainerElement component, HAPContextStructure extraContext, HAPRequirementContextProcessor contextProcessRequirement, HAPConfigureContextProcessor processConfigure) {
		HAPContextStructure parentContext = HAPUtilityContext.hardMerge(component.getContainer().getContextStructure(), extraContext); 
		return HAPProcessorContext.process(component.getElement().getContextStructure(), HAPParentContext.createDefault(parentContext), processConfigure, contextProcessRequirement);
	}

	
	public static void mergeWithParentAttachment(HAPWithAttachment withAttachment, HAPAttachmentContainer parentAttachment) {
		withAttachment.getAttachmentContainer().merge(parentAttachment, HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD);
	}
	
	public static HAPAttachmentContainer buildNameMappedAttachment(HAPAttachmentContainer attachment, HAPWithNameMapping withNameMapping) {
		HAPAttachmentContainer out = null;
		if(withNameMapping==null)   out = attachment;
		else out = withNameMapping.getNameMapping().mapAttachment(attachment);
		if(out==null)  out = new HAPAttachmentContainer();
		return out;
	}

	
	//build attachment mapping for internal component
	public static HAPAttachmentContainer buildInternalAttachment(HAPResourceId resourceId, HAPAttachmentContainer attachment, HAPWithNameMapping withNameMapping) {
		HAPAttachmentContainer out = buildNameMappedAttachment(attachment, withNameMapping); 
		if(resourceId!=null) {
			out.merge(new HAPAttachmentContainer(resourceId.getSupplement()), HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT);
		}
		return out;
	}
	

	
	public static void buildServiceChildrenComponent(HAPChildrenComponentIdContainer out, HAPWithServiceUse withServiceProvider, HAPAttachmentContainer attachment) {
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = withServiceProvider.getServiceProviderDefinitions(); 
		Map<String, HAPDefinitionServiceUse> serviceUseDefs = withServiceProvider.getServiceUseDefinitions();
		for(String serviceName : serviceUseDefs.keySet()) {
			HAPDefinitionServiceUse serviceUseDef = serviceUseDefs.get(serviceName);
			HAPDefinitionServiceProvider serviceProvider = allServiceProviders.get(serviceUseDef.getProvider());
			out.addChildCompoentId(new HAPChildrenComponentId(serviceName, HAPResourceIdFactory.newInstance(HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE, serviceProvider.getServiceId()), null), attachment);
		}
	}
}
