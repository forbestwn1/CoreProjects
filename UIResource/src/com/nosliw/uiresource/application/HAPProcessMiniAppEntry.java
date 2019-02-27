package com.nosliw.uiresource.application;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPDefinitionEmbededProcess;
import com.nosliw.data.core.process.HAPExecutableEmbededProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociationGroupWithTarget;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.module.HAPExecutableModule;
import com.nosliw.uiresource.module.HAPProcessorModule;
import com.nosliw.uiresource.module.HAPUtilityModule;

public class HAPProcessMiniAppEntry {

	public static HAPExecutableAppEntry process(
			HAPDefinitionApp miniAppDef,
			String entry, 
			HAPContextGroup parentContext, 
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
		HAPContextGroup entryContext = null;
		
		HAPContextGroup appContextGroup = new HAPContextGroup();
		Map<String, HAPDefinitionAppData> dataDef = miniAppDef.getDataDefinition();
		for(String dataName : dataDef.keySet()) {
			appContextGroup.setContext(dataName, dataDef.get(dataName));
		}
		out.setContext(HAPProcessorContext.process(entryDefinition.getContext(), parentContext==null?new HAPContextGroup():parentContext, contextProcessConfg, contextProcessRequirement));

		//process
		Map<String, HAPDefinitionEmbededProcess> processes = entryDefinition.getProcesses();
		for(String name : processes.keySet()) {
			HAPExecutableEmbededProcess processExe = HAPProcessorProcess.process(processes.get(name), name, entryContext, null, entryServiceProviders, processMan, contextProcessRequirement, processTracker);
			out.addProcess(name, processExe);
		}
		
		//module
		for(HAPDefinitionAppModule moduleDef : entryDefinition.getModules()) {
			HAPExecutableAppModule module = process(moduleDef, out, entryServiceProviders, processMan, uiResourceMan, contextProcessRequirement, processTracker);
			out.addUIModule(moduleDef.getName(), module);
		}
		
		
		return out;
	}
	
	private static HAPExecutableAppModule process(
			HAPDefinitionAppModule module,
			HAPExecutableAppEntry entryExe,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPUIResourceManager uiResourceMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableAppModule out = new HAPExecutableAppModule(module);
		
		HAPDefinitionModule moduleDef = HAPUtilityModule.getUIModuleDefinitionById(module.getModule(), uiResourceMan.getModuleParser());
		
		//input data association
		HAPExecutableDataAssociationGroupWithTarget inputMapping = HAPProcessorDataAssociation.processDataAssociation(entryExe.getContext(), module.getInputMapping(), moduleDef.getContext(), false, contextProcessRequirement);
		out.setInputMapping(inputMapping);
		
		//module
		HAPExecutableModule moduleExe = HAPProcessorModule.process(moduleDef, moduleDef.getName(), null, serviceProviders, processMan, uiResourceMan, contextProcessRequirement.dataTypeHelper, contextProcessRequirement.runtime, contextProcessRequirement.expressionManager, contextProcessRequirement.serviceDefinitionManager, processTracker);
		out.setModule(moduleExe);
		
		//output data association
		HAPExecutableDataAssociationGroupWithTarget outputMapping = HAPProcessorDataAssociation.processDataAssociation(entryExe.getContext(), module.getInputMapping(), moduleDef.getContext(), false, contextProcessRequirement);
		out.setOutputMapping(outputMapping);
		
		return out;
	}
	
}
