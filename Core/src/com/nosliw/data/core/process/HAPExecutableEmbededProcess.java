package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

//process that is part of system
//it should include data association that mapping result to system context 
public class HAPExecutableEmbededProcess extends HAPExecutableProcess{

	private Map<String, HAPBackToGlobalContext> m_backToGlobals;
	
	public HAPExecutableEmbededProcess(HAPDefinitionEmbededProcess definition, String id) {
		super(definition, id);
		this.m_backToGlobals = new LinkedHashMap<String, HAPBackToGlobalContext>();
	}

	public HAPDefinitionEmbededProcess getEmbededProcessDefinition() {   return (HAPDefinitionEmbededProcess)this.getDefinition();   }
	
	public void addBackToGlobalContext(String result, HAPBackToGlobalContext backToGlobalContext) {   this.m_backToGlobals.put(result, backToGlobalContext);  }
	
}
