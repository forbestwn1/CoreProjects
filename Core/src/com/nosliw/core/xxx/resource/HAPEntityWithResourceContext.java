package com.nosliw.core.xxx.resource;

public class HAPEntityWithResourceContext {

	private HAPContextResourceDefinition m_resourceContext;
	
	private Object m_entity;
	
	public HAPEntityWithResourceContext(Object entity, HAPContextResourceDefinition resourceContext) {
		this.m_entity = entity;
		this.m_resourceContext = resourceContext;
	}

	public HAPEntityWithResourceContext(Object entity) {
		this(entity, null);
	}
	
	public HAPContextResourceDefinition getResourceContext() {    return this.m_resourceContext;   }
	
	public Object getEntity() {   return this.m_entity;     }
	
}
