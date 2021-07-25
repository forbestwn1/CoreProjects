package com.nosliw.data.core.component.attachment;

import com.nosliw.data.core.component.HAPDefinitionEntityComplex;

public class HAPResultProcessAttachmentReference {

	//entity after solve attachment reference
	private Object m_entity;
	
	//adaptor for entity
	private Object m_adaptor;

	//context complex entity for solving attachment reference in entity 
	private HAPDefinitionEntityComplex m_contextComplexEntity;
	
	public HAPResultProcessAttachmentReference(Object entity, Object adaptor, HAPDefinitionEntityComplex contextComplexEntity) {
		this.m_entity = entity;
		this.m_adaptor = adaptor;
		this.m_contextComplexEntity = contextComplexEntity;
	}
	
	public Object getEntity() {   return this.m_entity;   }
	
	public Object getAdaptor() {   return this.m_adaptor;     }

	public HAPDefinitionEntityComplex getContextComplexEntity() {    return this.m_contextComplexEntity;   }
}
