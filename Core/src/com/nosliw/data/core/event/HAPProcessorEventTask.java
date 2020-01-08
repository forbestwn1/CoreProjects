package com.nosliw.data.core.event;

import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcessDefinition;
import com.nosliw.data.core.process.HAPUtilityProcess;

public class HAPProcessorEventTask {

	private HAPManagerProcessDefinition m_processDefMan;
	
	public HAPExecutableEventTask process(HAPDefinitionEventTask definition) {
		HAPExecutableEventTask out = new HAPExecutableEventTask(definition);
		
		HAPDefinitionEventSource source = definition.getEventSourceInfo();
		HAPDefinitionPollTask pollTaskDef = source.getPollTask();
		
		HAPDefinitionProcessSuite processSuite = HAPUtilityProcess.buildProcessSuiteFromAttachment(definition.getAttachmentContainer());
		HAPExecutableProcess pollProcess = this.m_processDefMan.getProcess(pollTaskDef.getProcess(), processSuite);
		
		HAPExecutablePollTask pollTask = new HAPExecutablePollTask(source.getPollTask());
		pollTask.setProcess(pollProcess);
		
		out.setPollTask(pollTask);
		
		return out;
	}
	
}
