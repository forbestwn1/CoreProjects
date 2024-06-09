package com.nosliw.core.application.common.valueport;

public class HAPInfoValuePort {

	private String m_type;
	
	private String m_ioDirection; 
	
	public HAPInfoValuePort(String type, String ioDirection) {
		this.m_type = type;
		this.m_ioDirection = ioDirection;
	}
	
	public String getType() {     return this.m_type;    }

	public String getIODirection() {     return this.m_ioDirection;     }
	public void setIODirection(String ioDirection) {   this.m_ioDirection = ioDirection;   }
	
}
