package com.nosliw.data.core.domain.entity.attachment1;

public class HAPInfoAttachment {

	private String m_valueType;
	
	private Object m_entity;
	
	private String m_name;
	
	public HAPInfoAttachment(String valueType, String name, Object entity) {
		this.m_valueType = valueType;
		this.m_name = name;
		this.m_entity = entity;
	}
	
	public String getName() {   return this.m_name;   }
	
	public String getValueType() {   return this.m_valueType;     }
	
	public Object getEntity() {    return this.m_entity;    }
}
