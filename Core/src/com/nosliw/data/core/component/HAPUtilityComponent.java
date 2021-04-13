package com.nosliw.data.core.component;

import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithDataContext;
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
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.resource.HAPResourceDefinitionContext;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceUse;

public class HAPUtilityComponent {

	public static void processComponentExecutable(
			HAPExecutableImpComponent componentExe,
			HAPContextGroup parentContext,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureContextProcessor contextProcessConfg,
			HAPManagerActivityPlugin activityPluginMan
			) {
		HAPComponent component = componentExe.getDefinition();
		
		//service providers
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(component.getAttachmentContainer(), null, runtimeEnv.getServiceManager().getServiceDefinitionManager()); 
		componentExe.setServiceProviders(allServiceProviders);
		
		//process context 
		componentExe.setContextStructure(HAPProcessorContext.process(component.getContextStructure(), HAPParentContext.createDefault(parentContext==null?new HAPContextGroup():parentContext), null, contextProcessConfg, runtimeEnv));
		
		//process process suite
		HAPResourceDefinitionProcessSuite processSuite = HAPUtilityProcessComponent.buildProcessSuiteFromComponent(component, activityPluginMan).cloneProcessSuiteDefinition(); 
		processSuite.setContextStructure(componentExe.getContextStructure());   //kkk
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
	
	public static HAPContextStructure resolveContextReference(HAPContextStructure context, List<HAPContextReference> contextRefs, HAPContainerAttachment attachments, HAPManagerResourceDefinition resourceDefMan) {
		String contextType = context.getType();
		
		for(HAPContextReference contextRef : contextRefs) {
			HAPAttachment att = attachments.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTEXT, contextRef.getName());
			
			//find target context
			HAPContext targetContext = null;
			if(contextType.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				HAPContextGroup contextGroup = (HAPContextGroup)context;
				String categary = contextRef.getCategary();
				if(HAPBasicUtility.isStringEmpty(categary))  categary = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
				targetContext = contextGroup.getChildContext(categary);
			}
			else {
				targetContext = (HAPContext)context;
			}
			
			//append referred context to target context
			HAPResourceDefinitionContext contextResourceDef = (HAPResourceDefinitionContext)HAPUtilityAttachment.getResourceDefinition(att, resourceDefMan);
			HAPContext referredContext = contextResourceDef.getContext();
			for(String eleName : referredContext.getElementNames()) {
				if(targetContext.getElement(eleName)==null) {
					targetContext.addElement(eleName, referredContext.getElement(eleName));
				}
			}
		}
		
		return context;
	}
	
	public static HAPContext getContext(Object def, HAPContext extraContext, HAPConfigureContextProcessor contextProcessConfig, HAPRuntimeEnvironment runtimeEnv) {
		HAPContext out = null;
		if(def instanceof HAPComponentContainerElement) {
			out = (HAPContext)HAPUtilityComponent.processElementComponentContext((HAPComponentContainerElement)def, extraContext, runtimeEnv, contextProcessConfig);
		}
		else if(def instanceof HAPWithDataContext){
			out = (HAPContext)HAPUtilityContext.hardMerge(((HAPWithDataContext)def).getContextStructure(), extraContext); 
		}
		return out;
	}

	
	public static HAPContextStructure processElementComponentContext(HAPComponentContainerElement component, HAPContextStructure extraContext, HAPRuntimeEnvironment runtimeEnv, HAPConfigureContextProcessor processConfigure) {
		HAPContextStructure parentContext = HAPUtilityContext.hardMerge(component.getResourceContainer().getContextStructure(), extraContext); 
		HAPContextStructure processedEleContext = HAPProcessorContext.process(component.getComponentEntity().getContextStructure(), HAPParentContext.createDefault(parentContext), component.getAttachmentContainer(), null, processConfigure, runtimeEnv);
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
