package com.nosliw.data.core.process;


public class HAPDefinitionProcessWithContext {

	//process itself
	private HAPDefinitionProcessWrapper m_process;
	
	//context that the process depend on
	private HAPContextProcessor m_context;
	
	public HAPDefinitionProcessWithContext(HAPDefinitionProcessWrapper processDef, HAPContextProcessor context) {
		this.m_process = processDef;
		this.m_context = context;
	}
	
	public HAPDefinitionProcessWithContext(HAPDefinitionProcessWrapper processDef) {
		this.m_process = processDef;
	}
	
	public HAPDefinitionProcessWrapper getProcess() {  return this.m_process;	}
	
	public HAPContextProcessor getContext() {  return this.m_context;   }
	
}
