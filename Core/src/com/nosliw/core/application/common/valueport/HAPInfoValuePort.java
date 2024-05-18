package com.nosliw.core.application.common.valueport;

public class HAPInfoValuePort {

	private String m_type;
	
	private String m_ioDirection; 
	
	public HAPInfoValuePort(String type) {
		this.m_type = type;
	}
	
	public String getType() {     return this.m_type;    }

	public String getIODirection() {     return null;     }
	public void setIODirection(String ioDirection) {   this.m_ioDirection = ioDirection;   }
	
}
