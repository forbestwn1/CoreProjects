package com.nosliw.data.core.event;

import java.util.Map;

import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.process1.HAPExecutableProcess;

//task to run poll event resource and generate event
public class HAPExecutablePollTask {

	private HAPDefinitionPollTask m_definition;
	
	private HAPExecutableWrapperTask<HAPExecutableProcess> m_process;
	
	public HAPExecutablePollTask(HAPDefinitionPollTask definition) {
		this.m_definition = definition;
	}
	
	public Map<String, HAPData> getPollInput(){   return this.m_definition.getInput();   }
	
	public HAPExecutableWrapperTask<HAPExecutableProcess> getProcess() {    return this.m_process;    }
	public void setProcess(HAPExecutableWrapperTask<HAPExecutableProcess> process) {   this.m_process = process;   }
}
