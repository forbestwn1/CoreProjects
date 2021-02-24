package com.nosliw.uiresource.module;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNosliwUtility;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPHandlerLifecycle;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.handler.HAPHandler;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.process.HAPUtilityProcessComponent;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionNode;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextStructureEmpty;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;

public class HAPProcessorModule {

	public static HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition, 
			String id, 
			HAPContextGroup parentContext, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan) {
		
		HAPProcessTracker processTracker = new HAPProcessTracker(); 
		return HAPProcessorModule.process(moduleDefinition, id, parentContext, null, runtimeEnv, uiResourceMan, processTracker);
	}
	
	private static HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition,
			String id, 
			HAPContextGroup parentContext, 
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan,
			HAPProcessTracker processTracker) {

		HAPExecutableModule out = new HAPExecutableModule(moduleDefinition, id);

		HAPRequirementContextProcessor contextProcessRequirement1 = HAPUtilityCommon.getDefaultContextProcessorRequirement(runtimeEnv.getResourceDefinitionManager(), runtimeEnv.getDataTypeHelper(), runtimeEnv.getRuntime(), runtimeEnv.getExpressionManager(), runtimeEnv.getServiceManager().getServiceDefinitionManager());
		HAPConfigureContextProcessor contextProcessConfg = HAPUtilityConfiguration.getContextProcessConfigurationForModule();
		
		//service providers
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(moduleDefinition.getAttachmentContainer(), serviceProviders, runtimeEnv.getServiceManager().getServiceDefinitionManager()); 
		
		//process context 
		out.setContextGroup((HAPContextGroup)HAPProcessorContext.process(moduleDefinition.getContextStructure(), HAPParentContext.createDefault(parentContext==null?new HAPContextGroup():parentContext), contextProcessConfg, runtimeEnv));

		//process process suite
		HAPDefinitionProcessSuite processSuite = HAPUtilityProcessComponent.buildProcessSuiteFromComponent(moduleDefinition, runtimeEnv.getProcessManager().getPluginManager()).cloneProcessSuiteDefinition(); 
		processSuite.setContextStructure(out.getContext());   //kkk
		out.setProcessSuite(processSuite);
		
		//process lifecycle action
		Set<HAPHandlerLifecycle> lifecycleActions = moduleDefinition.getLifecycleAction();
		for(HAPHandlerLifecycle action : lifecycleActions) {
			HAPDefinitionProcess processDef = out.getProcessDefinition(action.getTask().getTaskDefinition());
			HAPExecutableProcess processExe = HAPProcessorProcess.process(processDef, null, allServiceProviders, runtimeEnv.getProcessManager(), runtimeEnv, processTracker);
			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(action.getTask(), processExe, HAPParentContext.createDefault(out.getContext()), null, runtimeEnv);			
			out.addLifecycle(action.getName(), processExeWrapper);
		}
		
		//process ui
		for(HAPDefinitionModuleUI ui : moduleDefinition.getUIs()) {
			if(!HAPUtilityEntityInfo.isEnabled(ui)) {
				HAPExecutableModuleUI uiExe = processModuleUI(ui, ui.getName(), out, allServiceProviders, runtimeEnv.getProcessManager(), uiResourceMan, runtimeEnv, processTracker);
				out.addModuleUI(uiExe);
			}
		}
		
		return out;
	}
	
	private static HAPExecutableModuleUI processModuleUI(
			HAPDefinitionModuleUI moduleUIDefinition,
			String id,
			HAPExecutableModule moduleExe,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPUIResourceManager uiResourceMan,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPExecutableModuleUI out = new HAPExecutableModuleUI(moduleUIDefinition, id);
		
		//process page, use context in module override context in page
		//find pape resource id from attachment mapping
		HAPAttachmentReference pageAttachment = (HAPAttachmentReference)moduleExe.getDefinition().getAttachmentContainer().getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE, moduleUIDefinition.getPage());
		HAPResourceId pageResourceId = pageAttachment.getReferenceId();

		//attachment
//		HAPAttachmentContainer mappedParentAttachment = HAPComponentUtility.buildInternalAttachment(pageResourceId, moduleExe.getDefinition().getAttachmentContainer(), moduleUIDefinition);

		//ui decoration
		//ui decoration from page first
//		out.addUIDecoration(pageInfo.getDecoration());
		//ui decoration from module ui
		out.addUIDecoration(moduleUIDefinition.getUIDecoration());
		//ui decoration from module
		out.addUIDecoration(moduleExe.getDefinition().getUIDecoration());
		
		//context
		HAPContextGroup mappingContextGroup = new HAPContextGroup();
		HAPExecutableDataAssociation daEx = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping().getDefaultDataAssociation(), HAPParentContext.createDefault(HAPContextStructureEmpty.flatStructure()), null, runtimeEnv);
		mappingContextGroup.setContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, (HAPContext)daEx.getOutput().getOutputStructure());  //.getAssociation().getSolidContext());  kkkk

		HAPExecutableUIUnitPage page = uiResourceMan.getEmbededUIPage(pageResourceId, id, mappingContextGroup, null, moduleExe.getDefinition().getAttachmentContainer(), moduleUIDefinition);
		out.setPage(page);

		HAPInfo daConfigure = HAPProcessorDataAssociation.withModifyOutputStructureConfigureFalse(new HAPInfoImpSimple());
		//build input data association
		HAPExecutableDataAssociation inputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping().getDefaultDataAssociation(), HAPParentContext.createDefault(page.getBody().getFlatContext().getContext()), daConfigure, runtimeEnv);
		out.setInputMapping(inputDataAssocation);
		
		//build output data association
		HAPExecutableDataAssociation outputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(page.getBody().getContext()), moduleUIDefinition.getOutputMapping().getDefaultDataAssociation(), HAPParentContext.createDefault(moduleExe.getContext()), daConfigure, runtimeEnv);
		out.setOutputMapping(outputDataAssocation);
		
		//event handler
		Set<HAPHandler> eventHandlerDefs = moduleUIDefinition.getEventHandlers();
		for(HAPHandler eventHandlerDef : eventHandlerDefs) {
			String eventName = eventHandlerDef.getName();
			HAPContextDefinitionRoot eventRootNode = buildContextRootFromEvent(out.getPage().getBody().getEventDefinition(eventName));
			HAPContextGroup eventContext = new HAPContextGroup();
			eventContext.addElement(HAPNosliwUtility.buildNosliwFullName("EVENT"), eventRootNode, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			HAPDefinitionProcess processDef = moduleExe.getProcessDefinition(eventHandlerDef.getTask().getTaskDefinition());
			HAPExecutableProcess eventProcessor = HAPProcessorProcess.process(processDef, eventContext, serviceProviders, processMan, runtimeEnv, processTracker);
			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(eventHandlerDef.getTask(), eventProcessor, HAPParentContext.createDefault(moduleExe.getContext()), null, runtimeEnv);			
			out.addEventHandler(eventName, processExeWrapper);
		}	
		return out;
	}

	private static HAPContextDefinitionRoot buildContextRootFromEvent(HAPDefinitionUIEvent eventDef) {
		HAPContextDefinitionRoot root = new HAPContextDefinitionRoot();
		HAPContextDefinitionNode nodeEle = new HAPContextDefinitionNode();
		
		HAPContext dataDef = eventDef.getDataDefinition();
		for(String dataName : dataDef.getElementNames()) {
			nodeEle.addChild(dataName, dataDef.getElement(dataName).getDefinition());
		}
		root.setDefinition(nodeEle);
		return root;
	}

}
