package com.nosliw.data.core.event;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPDefinitionPollTask {

	private Map<String, HAPData> m_input;
	
	private HAPDefinitionWrapperTask<String> m_process;
	
	public HAPDefinitionWrapperTask<String> getProcess() {  return this.m_process;   }
	
	public Map<String, HAPData> getInput(){  return this.m_input;   }
}
