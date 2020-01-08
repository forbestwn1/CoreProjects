package com.nosliw.data.core.event;

import java.util.Map;

import com.nosliw.data.core.HAPData;

public class HAPDefinitionPollTask {

	private Map<String, HAPData> m_input;
	
	private String m_process;
	
	public String getProcess() {  return this.m_process;   }
	
	public Map<String, HAPData> getInput(){  return this.m_input;   }
}
