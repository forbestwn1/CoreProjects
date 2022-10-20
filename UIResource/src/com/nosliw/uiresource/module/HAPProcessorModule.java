package com.nosliw.uiresource.module;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.complex.HAPResultSolveReference;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.component.HAPProcessorComponent;
import com.nosliw.data.core.component.HAPProcessorEmbededComponent;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.domain.entity.valuestructure.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructureNode;
import com.nosliw.data.core.structure.HAPRequirementContextProcessor;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;

public class HAPProcessorModule {

	public static HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition, 
			String id, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan) {
		
		HAPProcessTracker processTracker = new HAPProcessTracker(); 
		return HAPProcessorModule.process(moduleDefinition, id, runtimeEnv, uiResourceMan, processTracker);
	}
	
	
	private static HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition,
			String id, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan,
			HAPProcessTracker processTracker) {

		HAPExecutableModule out = new HAPExecutableModule(moduleDefinition, id);

		HAPRequirementContextProcessor contextProcessRequirement1 = HAPUtilityCommon.getDefaultContextProcessorRequirement(runtimeEnv.getResourceDefinitionManager(), runtimeEnv.getDataTypeHelper(), runtimeEnv.getRuntime(), runtimeEnv.getExpressionManager(), runtimeEnv.getServiceManager().getServiceDefinitionManager());
		HAPConfigureProcessorValueStructure contextProcessConfg = HAPUtilityConfiguration.getContextProcessConfigurationForModule();

		HAPProcessorComponent.normalize(moduleDefinition, new HAPContextProcessor(moduleDefinition, runtimeEnv));
		
		HAPProcessorComponent.process(moduleDefinition, out, runtimeEnv);
		
		//attachment
//		HAPUtilityComponent.processAttachmentInChild(moduleDefinition, attachmentReferenceContext.getComplexEntity());
		
		//process value structure
//		HAPValueStructure processedValueStructure = (HAPValueStructure)HAPProcessorStructure.process(moduleDefinition.getValueStructure(), HAPContainerStructure.createDefault(parentValueStructure), moduleDefinition.getAttachmentContainer(), null, contextProcessConfg, runtimeEnv);
//		out.setValueStructure(processedValueStructure);

		HAPContextProcessor processContext = new HAPContextProcessor(moduleDefinition, runtimeEnv);
		
		//process ui
		for(HAPDefinitionModuleUI ui : moduleDefinition.getUIs()) {
			if(HAPUtilityEntityInfo.isEnabled(ui)) {
				HAPExecutableModuleUI uiExe = processModuleUI(ui, ui.getName(), out, processContext, uiResourceMan, processTracker);
				out.addModuleUI(uiExe);
			}
		}
		
		return out;
	}
	
	private static HAPExecutableModuleUI processModuleUI(
			HAPDefinitionModuleUI moduleUIDefinition,
			String id,
			HAPExecutableModule moduleExe,
			HAPContextProcessor processContext,
			HAPUIResourceManager uiResourceMan,
			HAPProcessTracker processTracker) {
		HAPExecutableModuleUI out = new HAPExecutableModuleUI(moduleUIDefinition, id);
		
		HAPRuntimeEnvironment runtimeEnv = processContext.getRuntimeEnvironment();
		
		//resolve page reference
		HAPResultSolveReference refSolveResult = HAPUtilityComponent.solveReference(moduleUIDefinition.getPage(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE, processContext);
		HAPExecutableUIUnitPage pageExe = uiResourceMan.getUIPage((HAPDefinitionUIUnitPage)refSolveResult.getValue(), id, processContext);
		out.setPage(pageExe);

		HAPProcessorEmbededComponent.process(moduleUIDefinition, out, pageExe.getBody(), moduleExe, runtimeEnv);
		
		//ui decoration
		//ui decoration from page first
//		out.addUIDecoration(pageInfo.getDecoration());
		//ui decoration from module ui
		out.addUIDecoration(moduleUIDefinition.getUIDecoration());
		//ui decoration from module
		out.addUIDecoration(moduleExe.getDefinition().getUIDecoration());
		
		//event handler
//		Set<HAPHandler> eventHandlerDefs = moduleUIDefinition.getEventHandlers();
//		for(HAPHandler eventHandlerDef : eventHandlerDefs) {
//			String eventName = eventHandlerDef.getName();
//			HAPRootStructure eventRootNode = buildContextRootFromEvent(out.getPage().getBody().getEventDefinition(eventName));
//			HAPValueStructureDefinitionGroup eventContext = new HAPValueStructureDefinitionGroup();
//			eventContext.addRoot(HAPNosliwUtility.buildNosliwFullName("EVENT"), eventRootNode, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
//			HAPResourceDefinitionProcess processDef = moduleExe.getProcessDefinition(eventHandlerDef.getTask().getTaskDefinition());
//			HAPExecutableProcess eventProcessor = HAPProcessorProcess.process(processDef, eventContext, serviceProviders, processMan, runtimeEnv, processTracker);
//			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(eventHandlerDef.getTask(), eventProcessor, HAPContainerStructure.createDefault(moduleExe.getContext()), null, runtimeEnv);			
//			out.addEventHandler(eventName, processExeWrapper);
//		}	
		

		return out;
	}

	private static HAPRootStructure buildContextRootFromEvent(HAPDefinitionUIEvent eventDef) {
		HAPRootStructure root = new HAPRootStructure();
		HAPElementStructureNode nodeEle = new HAPElementStructureNode();
		
		HAPValueStructureDefinitionFlat dataDef = eventDef.getDataDefinition();
		for(String dataName : dataDef.getRootNames()) {
			nodeEle.addChild(dataName, dataDef.getRoot(dataName).getDefinition());
		}
		root.setDefinition(nodeEle);
		return root;
	}

}
