package com.nosliw.data.core.resource;

public class HAPResourceDefinitionWithContext {

	private HAPContextResourceDefinition m_resourceContext;
	
	private HAPResourceDefinition m_resourceDefinition;
	
	public HAPResourceDefinitionWithContext(HAPResourceDefinition resourceDefinition, HAPContextResourceDefinition resourceContext) {
		this.m_resourceDefinition = resourceDefinition;
		this.m_resourceContext = resourceContext;
	}

	public HAPResourceDefinitionWithContext(HAPResourceDefinition resourceDefinition) {
		this(resourceDefinition, null);
	}
	
	public HAPContextResourceDefinition getResourceContext() {    return this.m_resourceContext;   }
	
	public HAPResourceDefinition getResourceDefinition() {   return this.m_resourceDefinition;     }
	
}
