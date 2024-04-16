package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;

public class HAPResultAttribute {

	private HAPAttributeInBrick m_attribute;
	
	private HAPPath m_remainPath;
	
	public HAPResultAttribute(HAPAttributeInBrick attribute, HAPPath remainPath) {
		this.m_attribute = attribute;
		this.m_remainPath = remainPath;
	}

	public HAPAttributeInBrick getAttribute() {		return this.m_attribute;	}
	
	public HAPPath getRemainPath() {   return this.m_remainPath;    }
}
