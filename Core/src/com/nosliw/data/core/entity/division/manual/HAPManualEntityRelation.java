package com.nosliw.data.core.entity.division.manual;

public class HAPManualEntityRelation {

	public static String TYPE = "type";
	
	private String m_type;
	
	public HAPManualEntityRelation(String type) {
		this.m_type = type;
	}
	
	public String getType() {    return this.m_type;    }
	
}
