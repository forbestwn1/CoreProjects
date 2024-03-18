package com.nosliw.core.application.division.manual;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPManualBrickRelation extends HAPSerializableImp{

	public static String TYPE = "type";
	
	private String m_type;
	
	public HAPManualBrickRelation(String type) {
		this.m_type = type;
	}
	
	public String getType() {    return this.m_type;    }
	
}
