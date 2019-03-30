package com.nosliw.uiresource.application;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.module.HAPDefinitionModuleUI;
import com.nosliw.uiresource.module.HAPExecutableModule;
import com.nosliw.uiresource.module.HAPProcessorModule;
import com.nosliw.uiresource.module.HAPUtilityModule;

public class HAPProcessMiniAppEntry {

	public static HAPExecutableAppEntry process(
			HAPDefinitionApp miniAppDef,
			String entry, 
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionManager,
			HAPManagerServiceDefinition serviceDefinitionManager,
			HAPProcessTracker processTracker) {

		HAPExecutableAppEntry out = new HAPExecutableAppEntry(entry, miniAppDef);
		
		HAPDefinitionAppEntryUI entryDefinition = miniAppDef.getEntry(entry);
		
		HAPRequirementContextProcessor contextProcessRequirement = HAPUtilityCommon.getDefaultContextProcessorRequirement(dataTypeHelper, runtime, expressionManager, serviceDefinitionManager);
		HAPConfigureContextProcessor contextProcessConfg = HAPUtilityApp.getContextProcessConfigurationForApp();
		
		//service providers
		Map<String, HAPDefinitionServiceProvider> entryServiceProviders = HAPUtilityServiceUse.buildServiceProvider(miniAppDef.getServiceProviderDefinitions(), entryDefinition, contextProcessRequirement.serviceDefinitionManager); 

		//context
		out.setContext(HAPProcessorContext.process(entryDefinition.getContext(), HAPParentContext.createDefault(miniAppDef.getContext()), contextProcessConfg, contextProcessRequirement));

		//data definition
		Map<String, HAPDefinitionAppData> dataDefs = miniAppDef.getDataDefinition();
		for(String dataDefName : dataDefs.keySet()) { 
			 out.addDataDefinition(dataDefName, HAPProcessorContext.process(dataDefs.get(dataDefName), HAPParentContext.createDefault(miniAppDef.getContext()), contextProcessConfg, contextProcessRequirement));
		}
		
		//process
		Map<String, HAPDefinitionWrapperTask<HAPDefinitionProcess>> processes = entryDefinition.getProcesses();
		for(String name : processes.keySet()) {
			HAPExecutableProcess processExe = HAPProcessorProcess.process(processes.get(name).getTaskDefinition(), name, out.getContext(), null, entryServiceProviders, processMan, contextProcessRequirement, processTracker);
			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(processes.get(name), processExe, HAPParentContext.createDefault(out.getContext()), null, contextProcessRequirement);			
			out.addProcess(name, processExeWrapper);
		}

		//prepare extra context
		Map<String, HAPContextStructure> extraContext = out.getExtraContext();
		
		//module
		for(HAPDefinitionAppModule moduleDef : entryDefinition.getModules()) {
			if(!HAPDefinitionModuleUI.STATUS_DISABLED.equals(moduleDef.getStatus())) {
				HAPExecutableAppModule module = process(moduleDef, out, extraContext, entryServiceProviders, processMan, uiResourceMan, contextProcessRequirement, processTracker);
				out.addUIModule(moduleDef.getName(), module);
			}
		}
		
		return out;
	}
	
	private static HAPExecutableAppModule process(
			HAPDefinitionAppModule module,
			HAPExecutableAppEntry entryExe,
			Map<String, HAPContextStructure> extraContexts,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPUIResourceManager uiResourceMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableAppModule out = new HAPExecutableAppModule(module);
		
		HAPDefinitionModule moduleDef = HAPUtilityModule.getUIModuleDefinitionById(module.getModule(), uiResourceMan.getModuleParser());
		 
		HAPParentContext parentContext = HAPParentContext.createDefault(entryExe.getContext());
		for(String extraName : extraContexts.keySet()) {
			parentContext.addContext(extraName, extraContexts.get(extraName));
		}
		
		HAPInfo daConfigure = HAPProcessorDataAssociation.withModifyStructureFalse(new HAPInfoImpSimple());

		//input data association
		Map<String, HAPDefinitionDataAssociation> inputDas = module.getInputMapping().getDataAssociations();
		for(String inputDaName : inputDas.keySet()) {
			HAPExecutableDataAssociation inputMapping = HAPProcessorDataAssociation.processDataAssociation(parentContext, inputDas.get(inputDaName), HAPParentContext.createDefault(moduleDef.getContext().getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC)), daConfigure, contextProcessRequirement);
			out.addInputDataAssociation(inputDaName, inputMapping);
		}
		
		//module
		HAPExecutableModule moduleExe = HAPProcessorModule.process(moduleDef, moduleDef.getName(), null, serviceProviders, processMan, uiResourceMan, contextProcessRequirement.dataTypeHelper, contextProcessRequirement.runtime, contextProcessRequirement.expressionManager, contextProcessRequirement.serviceDefinitionManager, processTracker);
		out.setModule(moduleExe);
		
		//output data association
		Map<String, HAPDefinitionDataAssociation> outputMapping = module.getOutputMapping().getDataAssociations();
		for(String outputTargetName : outputMapping.keySet()) {
			HAPExecutableDataAssociation outputMappingByTarget = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(moduleDef.getContext()), outputMapping.get(outputTargetName), parentContext, daConfigure, contextProcessRequirement);
			out.addOutputDataAssociation(outputTargetName, outputMappingByTarget);
		}
		return out;
	}
}
