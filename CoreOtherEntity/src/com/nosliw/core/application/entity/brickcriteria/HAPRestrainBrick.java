package com.nosliw.core.application.entity.brickcriteria;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPRestrainBrick extends HAPSerializableImp{

	public final static String TYPE = "type"; 

	private String m_type;
	
	public HAPRestrainBrick(String type) {
		this.m_type = type;
	}
	
	public String getType() {    return this.m_type;	}
	
//	String[] isValid(HAPBrick brick);
	
}
