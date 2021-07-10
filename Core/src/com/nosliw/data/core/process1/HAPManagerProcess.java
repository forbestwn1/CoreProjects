package com.nosliw.data.core.process1;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcess;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;

public class HAPManagerProcess {
	
	private HAPRuntimeEnvironment m_runtimeEnv;

	private HAPManagerActivityPlugin m_pluginManager;
	
	public HAPManagerProcess(
			HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}

	public HAPResourceDefinitionProcessSuite getProcessSuiteDefinition(HAPResourceId suiteId, HAPContainerAttachment parentAttachment) {
		HAPResourceDefinitionProcessSuite suiteDef = (HAPResourceDefinitionProcessSuite)this.getResourceDefinitionManager().getAdjustedComplextResourceDefinition(suiteId, parentAttachment);
		return suiteDef;
	}
	
	public HAPResourceDefinitionProcess getProcessDefinition(HAPResourceId processId, HAPContainerAttachment parentAttachment) {
		HAPResourceDefinitionProcess processDef = (HAPResourceDefinitionProcess)this.getResourceDefinitionManager().getAdjustedComplextResourceDefinition(processId, parentAttachment);
		return processDef;
	}
	
	public HAPEntityWithResourceContext getProcessDefinitionWithContext(HAPResourceId processId, HAPContainerAttachment parentAttachment) {
		HAPResourceDefinitionProcess processDef = this.getProcessDefinition(processId, parentAttachment);
		HAPEntityWithResourceContext out = new HAPEntityWithResourceContext(processDef, HAPContextProcessor.createContext(processDef.getSuite(), this.getResourceDefinitionManager()));
		return out;
	}
	
	public HAPExecutableProcess getProcess(HAPResourceId processId, HAPContextProcessor context) {
		if(context==null)  context = HAPContextProcessor.createContext(this.getResourceDefinitionManager());
		HAPExecutableProcess out = HAPProcessorProcess.process(processId.toStringValue(HAPSerializationFormat.LITERATE), context.getResourceDefinition(processId), null, null, this, this.m_runtimeEnv, new HAPProcessTracker());
		return out;
	}
	
	public HAPExecutableProcess getProcess(String processId, HAPResourceDefinitionProcessSuite suite) {
		HAPExecutableProcess out = HAPProcessorProcess.process(processId, new HAPEntityWithResourceContext(new HAPResourceDefinitionProcess(suite, processId)), null, null, this, this.m_runtimeEnv, new HAPProcessTracker());
		return out;
	}

	//process process by name from processor context
	public HAPExecutableWrapperTask<HAPExecutableProcess> getEmbededProcess(
			HAPResourceId processId, 
			HAPContextProcessor context, 
			HAPDefinitionDataAssociation inputMapping, 
			Map<String, HAPDefinitionDataAssociation> outputMapping,
			HAPContainerStructure inputContext, 
			Map<String, HAPContainerStructure> outputContext 
		) {
		HAPDefinitionWrapperTask<HAPEntityWithResourceContext> suiteWrapper = new HAPDefinitionWrapperTask(context.getResourceDefinition(processId));
		suiteWrapper.setInputMapping(inputMapping);
		suiteWrapper.setOutputMapping(outputMapping);
		HAPExecutableProcess processExe = this.getProcess(processId, context);
		HAPExecutableWrapperTask<HAPExecutableProcess> out = HAPProcessorDataAssociation.processDataAssociationWithTask(suiteWrapper, processExe, inputContext, outputContext, null, this.m_runtimeEnv);
		return out;
	}

	public HAPExecutableWrapperTask<HAPExecutableProcess> getEmbededProcess(
			HAPResourceId processId, 
			HAPContextProcessor context, 
			HAPDefinitionDataAssociation inputMapping, 
			Map<String, HAPDefinitionDataAssociation> outputMapping,
			HAPContainerStructure inputContext, 
			HAPContainerStructure outputContext 
		) {
		HAPDefinitionWrapperTask<HAPEntityWithResourceContext> suiteWrapper = new HAPDefinitionWrapperTask(context.getResourceDefinition(processId));
		suiteWrapper.setInputMapping(inputMapping);
		suiteWrapper.setOutputMapping(outputMapping);
		HAPExecutableProcess processExe = this.getProcess(processId, context);
		HAPExecutableWrapperTask<HAPExecutableProcess> out = HAPProcessorDataAssociation.processDataAssociationWithTask(suiteWrapper, processExe, inputContext, outputContext, null, this.m_runtimeEnv);
		return out;
	}
	
	public HAPExecutableWrapperTask<HAPExecutableProcess> getEmbededProcess(
			String processId, 
			HAPResourceDefinitionProcessSuite suite, 
			HAPDefinitionDataAssociation inputMapping, 
			Map<String, HAPDefinitionDataAssociation> outputMapping,
			HAPContainerStructure inputContext, 
			HAPContainerStructure outputContext 
		) {
		HAPDefinitionWrapperTask<HAPEntityWithResourceContext> suiteWrapper = new HAPDefinitionWrapperTask(new HAPEntityWithResourceContext(new HAPResourceDefinitionProcess(suite, processId)));
		suiteWrapper.setInputMapping(inputMapping);
		suiteWrapper.setOutputMapping(outputMapping);
		HAPExecutableProcess processExe = this.getProcess(processId, suite);
		HAPExecutableWrapperTask<HAPExecutableProcess> out = HAPProcessorDataAssociation.processDataAssociationWithTask(suiteWrapper, processExe, inputContext, outputContext, null, this.m_runtimeEnv);
		return out;
	}

	public HAPExecutableWrapperTask<HAPExecutableProcess> getEmbededProcess(
			String processId, 
			HAPResourceDefinitionProcessSuite suite, 
			HAPDefinitionDataAssociation inputMapping, 
			Map<String, HAPDefinitionDataAssociation> outputMapping,
			HAPContainerStructure inputContext, 
			Map<String, HAPContainerStructure> outputContext 
		) {
		HAPDefinitionWrapperTask<HAPEntityWithResourceContext> suiteWrapper = new HAPDefinitionWrapperTask(new HAPEntityWithResourceContext(new HAPResourceDefinitionProcess(suite, processId)));
		suiteWrapper.setInputMapping(inputMapping);
		suiteWrapper.setOutputMapping(outputMapping);
		HAPExecutableProcess processExe = this.getProcess(processId, suite);
		HAPExecutableWrapperTask<HAPExecutableProcess> out = HAPProcessorDataAssociation.processDataAssociationWithTask(suiteWrapper, processExe, inputContext, outputContext, null, this.m_runtimeEnv);
		return out;
	}

	public HAPManagerActivityPlugin getPluginManager() {   return this.m_pluginManager;    }
	
	private HAPManagerResourceDefinition getResourceDefinitionManager() {     return this.m_runtimeEnv.getResourceDefinitionManager();   }

}
