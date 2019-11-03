package com.nosliw.data.core.process;


public class HAPDefinitionProcessWithContext {

	//process itself
	private HAPDefinitionProcess m_process;
	
	//context that the process depend on
	private HAPContextProcessor m_context;
	
	public HAPDefinitionProcessWithContext(HAPDefinitionProcess process, HAPContextProcessor context) {
		this.m_process = process;
		this.m_context = context;
	}
	
	public HAPDefinitionProcess getProcess() {  return this.m_process;	}
	
	public HAPContextProcessor getContext() {  return this.m_context;   }
	
	
}
