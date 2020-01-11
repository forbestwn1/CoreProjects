package com.nosliw.data.core.event;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.process.HAPExecutableProcess;

//task to run poll event resource and generate event
public class HAPExecutablePollTask {

	private HAPDefinitionPollTask m_definition;
	
	private HAPExecutableProcess m_process;
	
	public HAPExecutablePollTask(HAPDefinitionPollTask definition) {
		this.m_definition = definition;
	}
	
	public HAPPollTaskResult execute(Map<String, HAPData> input) {
//		HAPServiceData serviceData = this.m_processRuntime.executeProcess(m_process, input);
		return null;
	}

	public Map<String, HAPData> getPollInput(){   return this.m_definition.getInput();   }
	
	public HAPExecutableProcess getProcess() {    return this.m_process;    }
	public void setProcess(HAPExecutableProcess process) {   this.m_process = process;   }
}
