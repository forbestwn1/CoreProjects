package com.nosliw.data.core.event;

import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPDefinitionProcessWithContext;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcessDefinition;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;

public class HAPProcessorEventTask {

	private HAPManagerProcessDefinition m_processDefMan;
	
	public HAPExecutableEventTask process(HAPDefinitionEventTask definition) {
		HAPExecutableEventTask out = new HAPExecutableEventTask(definition);
		
		HAPDefinitionEventSource source = definition.getEventSourceInfo();
		HAPDefinitionPollTask pollTaskDef = source.getPollTask();
		
		HAPDefinitionProcessSuite processSuite = HAPUtilityProcess.buildProcessSuiteFromAttachment(definition.getAttachmentContainer());
		HAPExecutableProcess pollProcess = this.m_processDefMan.getProcess(pollTaskDef.getProcess(), processSuite);
		
		HAPExecutableProcess processExe = HAPProcessorProcess.process(action.getName(), new HAPDefinitionProcessWithContext(processDef), out.getContext(), allServiceProviders, processMan, contextProcessRequirement, processTracker);
		HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(action.getProcess(), processExe, HAPParentContext.createDefault(out.getContext()), null, contextProcessRequirement);			

		
		HAPExecutablePollTask pollTask = new HAPExecutablePollTask(source.getPollTask());
		pollTask.setProcess(pollProcess);
		
		out.setPollTask(pollTask);
		
		return out;
	}
	
}
