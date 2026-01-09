package com.nosliw.core.application.dynamic;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPDynamicDefinitionCriteria extends HAPSerializableImp{

	public final static String TYPE = "type"; 

	private String m_type;
	
	public HAPDynamicDefinitionCriteria(String type) {
		this.m_type = type;
	}
	
	public String getType() {
		return this.m_type;
	}

}
