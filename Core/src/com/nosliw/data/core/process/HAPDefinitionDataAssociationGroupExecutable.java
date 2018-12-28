package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextFlat;

public class HAPDefinitionDataAssociationGroupExecutable {

	private HAPDefinitionDataAssociationGroup m_definition;
	
	//process purpose
	private HAPContextFlat m_context;
	
	//mapping from in path to out path, it is for runtime 
	private Map<String, String> m_pathMapping;
	
	public HAPDefinitionDataAssociationGroupExecutable(HAPDefinitionDataAssociationGroup definition) {
		this.m_definition = definition;
	}

	public HAPContextFlat getContext() {   return this.m_context;   }
	public void setContext(HAPContextFlat context) {   this.m_context = context;   }
}
