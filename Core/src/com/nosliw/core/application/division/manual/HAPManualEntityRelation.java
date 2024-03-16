package com.nosliw.core.application.division.manual;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPManualEntityRelation extends HAPSerializableImp{

	public static String TYPE = "type";
	
	private String m_type;
	
	public HAPManualEntityRelation(String type) {
		this.m_type = type;
	}
	
	public String getType() {    return this.m_type;    }
	
}
