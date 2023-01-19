package com.nosliw.data.core.component;

import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;
import com.nosliw.data.core.process1.HAPUtilityProcessComponent;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPExecutableImpComponent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceUse;
import com.nosliw.data.core.structure.temp.HAPProcessorContext;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPUtilityComponent {

	public static void processAttachmentInChild(HAPWithAttachment child, HAPWithAttachment parent) {
		HAPUtilityComponent.mergeWithParentAttachment(child, parent==null?null:parent.getAttachmentContainer());
	}
	
	public static void processComponentExecutable(
			HAPExecutableImpComponent componentExe,
			HAPValueStructureDefinitionGroup parentContext,
			HAPRuntimeEnvironment runtimeEnv,
			HAPConfigureProcessorValueStructure contextProcessConfg,
			HAPManagerActivityPlugin activityPluginMan
			) {
		HAPDefinitionEntityComponent component = componentExe.getDefinition();
		
		//service providers
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(component.getAttachmentContainer(), null, runtimeEnv.getServiceManager().getServiceDefinitionManager()); 
		componentExe.setServiceProviders(allServiceProviders);
		
		//process context 
		componentExe.setValueStructureDefinitionWrapper(HAPProcessorContext.process(component.getValueStructureWrapper(), HAPContainerStructure.createDefault(parentContext==null?new HAPValueStructureDefinitionGroup():parentContext), null, contextProcessConfg, runtimeEnv));
		
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
	
	public static HAPValueStructure resolveContextReference(HAPValueStructure context, List<HAPContextReference> contextRefs, HAPDefinitionEntityContainerAttachment attachments, HAPManagerResourceDefinition resourceDefMan) {
//		String contextType = context.getType();
//		
//		for(HAPContextReference contextRef : contextRefs) {
//			HAPAttachment att = attachments.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTEXT, contextRef.getName());
//			
//			//find target context
//			HAPValueStructureDefinitionFlat targetContext = null;
//			if(contextType.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
//				HAPValueStructureDefinitionGroup contextGroup = (HAPValueStructureDefinitionGroup)context;
//				String categary = contextRef.getCategary();
//				if(HAPBasicUtility.isStringEmpty(categary))  categary = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
//				targetContext = contextGroup.getChildContext(categary);
//			}
//			else {
//				targetContext = (HAPValueStructureDefinitionFlat)context;
//			}
//			
//			//append referred context to target context
//			HAPResourceDefinitionContext contextResourceDef = (HAPResourceDefinitionContext)HAPUtilityAttachment.getResourceDefinition(att, resourceDefMan);
//			HAPValueStructureDefinitionFlat referredContext = contextResourceDef.getContext();
//			for(String eleName : referredContext.getRootNames()) {
//				if(targetContext.getRoot(eleName)==null) {
//					targetContext.addRoot(eleName, referredContext.getRoot(eleName));
//				}
//			}
//		}
//		
//		return context;
		return null;
	}
	
	public static HAPDefinitionWrapperValueStructure getValueStructure(Object def, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionWrapperValueStructure out = null;
		if(def instanceof HAPDefinitionEntityElementInContainerComponent) {
			out = ((HAPDefinitionEntityElementInContainerComponent)def).getValueStructureWrapper();
		}
		else if(def instanceof HAPWithValueContext){
			out = ((HAPWithValueContext)def).getValueStructureWrapper();
		}
		return out;
	}

	
//	public static HAPValueStructure processElementComponentContext(HAPComponentContainerElement component, HAPValueStructure extraContext, HAPRuntimeEnvironment runtimeEnv, HAPConfigureProcessorStructure processConfigure) {
//		HAPValueStructure parentContext = HAPUtilityValueStructure.hardMerge(component.getResourceContainer().getValueStructureWrapper().getValueStructure(), extraContext); 
//		HAPValueStructure processedEleContext = HAPProcessorContext.process(component.getValueStructureWrapper().getValueStructure(), HAPContainerStructure.createDefault(parentContext), component.getAttachmentContainer(), null, processConfigure, runtimeEnv);
//		component.getValueStructureWrapper().setValueStructure(processedEleContext);
//		return HAPUtilityValueStructure.hardMerge(parentContext, processedEleContext);
//	}

	//parent attachment merge to child, child attachment has higher priority
	public static void mergeWithParentAttachment(HAPWithAttachment withAttachment, HAPDefinitionEntityContainerAttachment parentAttachment) {
		withAttachment.getAttachmentContainer().merge(parentAttachment, HAPConstant.INHERITMODE_CHILD);
	}

	public static HAPDefinitionEntityContainerAttachment buildNameMappedAttachment(HAPDefinitionEntityContainerAttachment attachment, HAPWithNameMapping withNameMapping) {
		HAPDefinitionEntityContainerAttachment out = null;
		if(withNameMapping==null)   out = attachment;
		else out = withNameMapping.getNameMapping().mapAttachment(attachment);
		if(out==null)  out = new HAPDefinitionEntityContainerAttachment();
		return out;
	}

	//build attachment mapping for internal component
	public static HAPDefinitionEntityContainerAttachment buildInternalAttachment(HAPResourceId resourceId, HAPDefinitionEntityContainerAttachment attachment, HAPWithNameMapping withNameMapping) {
		HAPDefinitionEntityContainerAttachment out = buildNameMappedAttachment(attachment, withNameMapping); 
		HAPUtilityAttachment.mergeAttachmentInResourceIdSupplementToContainer(resourceId, out, HAPConstant.INHERITMODE_PARENT);
		return out;
	}
	
	public static void buildServiceChildrenComponent(HAPContainerChildReferenceResource out, HAPWithServiceUse withServiceProvider, HAPDefinitionEntityContainerAttachment attachment) {
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = withServiceProvider.getServiceProviderDefinitions(); 
		Map<String, HAPDefinitionServiceUse> serviceUseDefs = withServiceProvider.getServiceUseDefinitions();
		for(String serviceName : serviceUseDefs.keySet()) {
			HAPDefinitionServiceUse serviceUseDef = serviceUseDefs.get(serviceName);
			HAPDefinitionServiceProvider serviceProvider = allServiceProviders.get(serviceUseDef.getProvider());
			out.addChildCompoentId(new HAPInfoChildResource(serviceName, HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE, serviceProvider.getServiceId()), null), attachment);
		}
	}
}
