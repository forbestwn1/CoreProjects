package com.nosliw.data.core.domain.valueport;

public class HAPIdValuePort {

	private String m_entityId;
	
	private String m_portType;
	
	private String m_valuePortName;
	
	public HAPIdValuePort(String entityId, String portType, String valuePortName) {
		this.m_entityId = entityId;
		this.m_portType = portType;
		this.m_valuePortName = valuePortName;
	}
	
	//which entity this value port belong
	public String getEntityId() {    return this.m_entityId;     }
	
	public String getPortType() {    return this.m_portType;     }
	
	//name of the port within entity
	public String getValuePortName() {    return this.m_valuePortName;     }
	
}
