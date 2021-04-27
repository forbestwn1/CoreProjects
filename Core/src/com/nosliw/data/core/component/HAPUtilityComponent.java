package com.nosliw.data.core.component;

import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.component.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.process.HAPUtilityProcessComponent;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPExecutableImpComponent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceUse;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPProcessorContext;
import com.nosliw.data.core.structure.HAPUtilityContext;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinition;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionGroup;
import com.nosliw.data.core.structure.value.resource.HAPResourceDefinitionContext;

public class HAPUtilityComponent {

	public static void processComponentExecutable(
			HAPExecutableImpComponent componentExe,
			HAPContextStructureValueDefinitionGroup parentContext,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorStructure contextProcessConfg,
			HAPManagerActivityPlugin activityPluginMan
			) {
		HAPComponent component = componentExe.getDefinition();
		
		//service providers
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(component.getAttachmentContainer(), null, runtimeEnv.getServiceManager().getServiceDefinitionManager()); 
		componentExe.setServiceProviders(allServiceProviders);
		
		//process context 
		componentExe.setValueContext(HAPProcessorContext.process(component.getValueContext(), HAPParentContext.createDefault(parentContext==null?new HAPContextStructureValueDefinitionGroup():parentContext), null, contextProcessConfg, runtimeEnv));
		
		//process process suite
		HAPResourceDefinitionProcessSuite processSuite = HAPUtilityProcessComponent.buildProcessSuiteFromComponent(component, activityPluginMan).cloneProcessSuiteDefinition(); 
		processSuite.setValueContext(componentExe.getContextStructure());   //kkk
		componentExe.setProcessSuite(processSuite);
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
	
	public static HAPContextStructureValueDefinition resolveContextReference(HAPContextStructureValueDefinition context, List<HAPContextReference> contextRefs, HAPContainerAttachment attachments, HAPManagerResourceDefinition resourceDefMan) {
		String contextType = context.getType();
		
		for(HAPContextReference contextRef : contextRefs) {
			HAPAttachment att = attachments.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTEXT, contextRef.getName());
			
			//find target context
			HAPContextStructureValueDefinitionFlat targetContext = null;
			if(contextType.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				HAPContextStructureValueDefinitionGroup contextGroup = (HAPContextStructureValueDefinitionGroup)context;
				String categary = contextRef.getCategary();
				if(HAPBasicUtility.isStringEmpty(categary))  categary = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
				targetContext = contextGroup.getChildContext(categary);
			}
			else {
				targetContext = (HAPContextStructureValueDefinitionFlat)context;
			}
			
			//append referred context to target context
			HAPResourceDefinitionContext contextResourceDef = (HAPResourceDefinitionContext)HAPUtilityAttachment.getResourceDefinition(att, resourceDefMan);
			HAPContextStructureValueDefinitionFlat referredContext = contextResourceDef.getContext();
			for(String eleName : referredContext.getElementNames()) {
				if(targetContext.getElement(eleName)==null) {
					targetContext.addElement(eleName, referredContext.getElement(eleName));
				}
			}
		}
		
		return context;
	}
	
	public static HAPContextStructureValueDefinition getContext(Object def, HAPContextStructureValueDefinition extraContext, HAPConfigureProcessorStructure contextProcessConfig, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextStructureValueDefinition out = null;
		if(def instanceof HAPComponentContainerElement) {
			out = HAPUtilityComponent.processElementComponentContext((HAPComponentContainerElement)def, extraContext, runtimeEnv, contextProcessConfig);
		}
		else if(def instanceof HAPWithValueContext){
			out = HAPUtilityContext.hardMerge(((HAPWithValueContext)def).getValueContext(), extraContext); 
		}
		return out;
	}

	
	public static HAPContextStructureValueDefinition processElementComponentContext(HAPComponentContainerElement component, HAPContextStructureValueDefinition extraContext, HAPRuntimeEnvironment runtimeEnv, HAPConfigureProcessorStructure processConfigure) {
		HAPContextStructureValueDefinition parentContext = HAPUtilityContext.hardMerge(component.getResourceContainer().getValueContext(), extraContext); 
		HAPContextStructureValueDefinition processedEleContext = HAPProcessorContext.process(component.getComponentEntity().getValueContext(), HAPParentContext.createDefault(parentContext), component.getAttachmentContainer(), null, processConfigure, runtimeEnv);
		return HAPUtilityContext.hardMerge(parentContext, processedEleContext);
	}

	//parent attachment merge to child, child attachment has higher priority
	public static void mergeWithParentAttachment(HAPWithAttachment withAttachment, HAPContainerAttachment parentAttachment) {
		withAttachment.getAttachmentContainer().merge(parentAttachment, HAPConstant.INHERITMODE_CHILD);
	}

	public static HAPContainerAttachment buildNameMappedAttachment(HAPContainerAttachment attachment, HAPWithNameMapping withNameMapping) {
		HAPContainerAttachment out = null;
		if(withNameMapping==null)   out = attachment;
		else out = withNameMapping.getNameMapping().mapAttachment(attachment);
		if(out==null)  out = new HAPContainerAttachment();
		return out;
	}

	//build attachment mapping for internal component
	public static HAPContainerAttachment buildInternalAttachment(HAPResourceId resourceId, HAPContainerAttachment attachment, HAPWithNameMapping withNameMapping) {
		HAPContainerAttachment out = buildNameMappedAttachment(attachment, withNameMapping); 
		HAPUtilityAttachment.mergeAttachmentInResourceIdSupplementToContainer(resourceId, out, HAPConstant.INHERITMODE_PARENT);
		return out;
	}
	
	public static void buildServiceChildrenComponent(HAPContainerChildReferenceResource out, HAPWithServiceUse withServiceProvider, HAPContainerAttachment attachment) {
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = withServiceProvider.getServiceProviderDefinitions(); 
		Map<String, HAPDefinitionServiceUse> serviceUseDefs = withServiceProvider.getServiceUseDefinitions();
		for(String serviceName : serviceUseDefs.keySet()) {
			HAPDefinitionServiceUse serviceUseDef = serviceUseDefs.get(serviceName);
			HAPDefinitionServiceProvider serviceProvider = allServiceProviders.get(serviceUseDef.getProvider());
			out.addChildCompoentId(new HAPInfoChildResource(serviceName, HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE, serviceProvider.getServiceId()), null), attachment);
		}
	}
}
