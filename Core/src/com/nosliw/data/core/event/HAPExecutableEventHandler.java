package com.nosliw.data.core.event;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPRuntimeProcess;

public class HAPExecutableEventHandler {

	private HAPRuntimeProcess m_processRuntime;
	
	private HAPDefinitionEventHandle m_eventHandlerDefinition;
	
	private HAPExecutableProcess m_process;
	
	public HAPPollTaskResult execute(Map<String, HAPData> input) {
//		HAPServiceData serviceData = this.m_processRuntime.executeProcess(m_process, input);
		return null;
	}
}