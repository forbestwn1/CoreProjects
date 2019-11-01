package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;

public class HAPManagerProcessDefinition {
	
	private HAPManagerActivityPlugin m_pluginManager;
	
	private HAPRequirementContextProcessor m_contextProcessRequirement;
	
	public HAPManagerProcessDefinition(
			HAPManagerActivityPlugin pluginMan,
			HAPDataTypeHelper dataTypeHelper,
			HAPRuntime runtime,
			HAPExpressionSuiteManager expressionManager,
			HAPManagerServiceDefinition serviceDefinitionManager) {
		this.m_pluginManager = pluginMan;
		this.m_contextProcessRequirement = new HAPRequirementContextProcessor(dataTypeHelper, runtime, expressionManager, serviceDefinitionManager, null);
	}

	public HAPExecutableProcess getProcess(HAPIdProcess processId) {
		HAPDefinitionProcessSuite suite = HAPUtilityProcess.getProcessSuite(processId.getSuiteId(), this.getPluginManager());
		HAPExecutableProcess out = HAPProcessorProcess.process(processId.getProcessId(), suite, null, this, this.m_contextProcessRequirement, new HAPProcessTracker());
		return out;
	}

	public HAPExecutableProcess getProcess(String processId, HAPDefinitionProcessSuite suite) {
		HAPExecutableProcess out = HAPProcessorProcess.process(processId, suite, null, this, this.m_contextProcessRequirement, new HAPProcessTracker());
		return out;
	}
	
	public HAPExecutableWrapperTask getEmbededProcess(
			String processId, 
			HAPDefinitionProcessSuite suite, 
			HAPDefinitionDataAssociation inputMapping, 
			Map<String, HAPDefinitionDataAssociation> outputMapping,
			HAPParentContext inputContext, 
			HAPParentContext outputContext 
		) {
		HAPDefinitionWrapperTask<HAPDefinitionProcess> suiteWrapper = new HAPDefinitionWrapperTask(suite.getProcess(processId));
		suiteWrapper.setInputMapping(inputMapping);
		suiteWrapper.setOutputMapping(outputMapping);
		HAPExecutableProcess processExe = this.getProcess(processId, suite);
		HAPExecutableWrapperTask out = HAPProcessorDataAssociation.processDataAssociationWithTask(suiteWrapper, processExe, inputContext, outputContext, null, this.m_contextProcessRequirement);
		return out;
	}

	public HAPExecutableWrapperTask getEmbededProcess(
			String processId, 
			HAPDefinitionProcessSuite suite, 
			HAPDefinitionDataAssociation inputMapping, 
			Map<String, HAPDefinitionDataAssociation> outputMapping,
			HAPParentContext inputContext, 
			Map<String, HAPParentContext> outputContext 
		) {
		HAPDefinitionWrapperTask<HAPDefinitionProcess> suiteWrapper = new HAPDefinitionWrapperTask(suite.getProcess(processId));
		suiteWrapper.setInputMapping(inputMapping);
		suiteWrapper.setOutputMapping(outputMapping);
		HAPExecutableProcess processExe = this.getProcess(processId, suite);
		HAPExecutableWrapperTask out = HAPProcessorDataAssociation.processDataAssociationWithTask(suiteWrapper, processExe, inputContext, outputContext, null, this.m_contextProcessRequirement);
		return out;
	}

	public HAPManagerActivityPlugin getPluginManager() {   return this.m_pluginManager;    }
	
}
