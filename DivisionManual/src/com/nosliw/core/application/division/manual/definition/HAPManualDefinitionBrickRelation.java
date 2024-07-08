package com.nosliw.core.application.division.manual.definition;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPManualDefinitionBrickRelation extends HAPSerializableImp{

	public static String TYPE = "type";
	
	private String m_type;
	
	public HAPManualDefinitionBrickRelation(String type) {
		this.m_type = type;
	}
	
	public String getType() {    return this.m_type;    }
	
}
