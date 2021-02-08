package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;

public class HAPManagerProcess {
	
	private HAPRuntimeEnvironment m_runtimeEnv;

	private HAPManagerActivityPlugin m_pluginManager;
	
	public HAPManagerProcess(
			HAPManagerActivityPlugin pluginMan,
			HAPRuntimeEnvironment runtimeEnv) {
		this.m_pluginManager = pluginMan;
		this.m_runtimeEnv = runtimeEnv;
	}

	public HAPDefinitionProcessSuite getProcessSuiteDefinition(HAPResourceId suiteId, HAPContainerAttachment parentAttachment) {
		HAPDefinitionProcessSuite suiteDef = (HAPDefinitionProcessSuite)this.getResourceDefinitionManager().getAdjustedComplextResourceDefinition(suiteId, parentAttachment);
		return suiteDef;
	}
	
	public HAPDefinitionProcess getProcessDefinition(HAPResourceId processId, HAPContainerAttachment parentAttachment) {
		HAPDefinitionProcess processDef = (HAPDefinitionProcess)this.getResourceDefinitionManager().getAdjustedComplextResourceDefinition(processId, parentAttachment);
		return processDef;
	}
	
	public HAPEntityWithResourceContext getProcessDefinitionWithContext(HAPResourceId processId, HAPContainerAttachment parentAttachment) {
		HAPDefinitionProcess processDef = this.getProcessDefinition(processId, parentAttachment);
		HAPEntityWithResourceContext out = new HAPEntityWithResourceContext(processDef, HAPContextProcessor.createContext(processDef.getSuite(), this.getResourceDefinitionManager()));
		return out;
	}
	
	public HAPExecutableProcess getProcess(HAPResourceId processId, HAPContextProcessor context) {
		if(context==null)  context = HAPContextProcessor.createContext(this.getResourceDefinitionManager());
		HAPExecutableProcess out = HAPProcessorProcess.process(processId.toStringValue(HAPSerializationFormat.LITERATE), context.getResourceDefinition(processId), null, null, this, this.m_runtimeEnv, new HAPProcessTracker());
		return out;
	}
	
	public HAPExecutableProcess getProcess(String processId, HAPDefinitionProcessSuite suite) {
		HAPExecutableProcess out = HAPProcessorProcess.process(processId, new HAPEntityWithResourceContext(new HAPDefinitionProcess(suite, processId)), null, null, this, this.m_runtimeEnv, new HAPProcessTracker());
		return out;
	}

	//process process by name from processor context
	public HAPExecutableWrapperTask<HAPExecutableProcess> getEmbededProcess(
			HAPResourceId processId, 
			HAPContextProcessor context, 
			HAPDefinitionDataAssociation inputMapping, 
			Map<String, HAPDefinitionDataAssociation> outputMapping,
			HAPParentContext inputContext, 
			Map<String, HAPParentContext> outputContext 
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
			HAPParentContext inputContext, 
			HAPParentContext outputContext 
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
			HAPDefinitionProcessSuite suite, 
			HAPDefinitionDataAssociation inputMapping, 
			Map<String, HAPDefinitionDataAssociation> outputMapping,
			HAPParentContext inputContext, 
			HAPParentContext outputContext 
		) {
		HAPDefinitionWrapperTask<HAPEntityWithResourceContext> suiteWrapper = new HAPDefinitionWrapperTask(new HAPEntityWithResourceContext(new HAPDefinitionProcess(suite, processId)));
		suiteWrapper.setInputMapping(inputMapping);
		suiteWrapper.setOutputMapping(outputMapping);
		HAPExecutableProcess processExe = this.getProcess(processId, suite);
		HAPExecutableWrapperTask<HAPExecutableProcess> out = HAPProcessorDataAssociation.processDataAssociationWithTask(suiteWrapper, processExe, inputContext, outputContext, null, this.m_runtimeEnv);
		return out;
	}

	public HAPExecutableWrapperTask<HAPExecutableProcess> getEmbededProcess(
			String processId, 
			HAPDefinitionProcessSuite suite, 
			HAPDefinitionDataAssociation inputMapping, 
			Map<String, HAPDefinitionDataAssociation> outputMapping,
			HAPParentContext inputContext, 
			Map<String, HAPParentContext> outputContext 
		) {
		HAPDefinitionWrapperTask<HAPEntityWithResourceContext> suiteWrapper = new HAPDefinitionWrapperTask(new HAPEntityWithResourceContext(new HAPDefinitionProcess(suite, processId)));
		suiteWrapper.setInputMapping(inputMapping);
		suiteWrapper.setOutputMapping(outputMapping);
		HAPExecutableProcess processExe = this.getProcess(processId, suite);
		HAPExecutableWrapperTask<HAPExecutableProcess> out = HAPProcessorDataAssociation.processDataAssociationWithTask(suiteWrapper, processExe, inputContext, outputContext, null, this.m_runtimeEnv);
		return out;
	}

	public HAPManagerActivityPlugin getPluginManager() {   return this.m_pluginManager;    }
	
	private HAPManagerResourceDefinition getResourceDefinitionManager() {     return this.m_runtimeEnv.getResourceDefinitionManager();   }

}
