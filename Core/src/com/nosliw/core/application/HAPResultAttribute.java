package com.nosliw.core.application;

public class HAPResultAttribute {

	private HAPAttributeInBrick m_attribute;
	
//	private HAPPath m_remainPath;
	
	public HAPResultAttribute(HAPAttributeInBrick attribute) {
		this.m_attribute = attribute;
	}

	public HAPAttributeInBrick getAttribute() {		return this.m_attribute;	}
	
//	public HAPPath getRemainPath() {   return this.m_remainPath;    }
}
