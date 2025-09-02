package com.nosliw.core.application.bricktypefacade;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPFacadeBrickTypeComplex extends HAPSerializableImp implements HAPFacadeBrickType{

	private String m_facade;
	
	public HAPFacadeBrickTypeComplex(String facade) {
		this.m_facade = facade;
	}

	@Override
	public String getType() {
		return null;
	}
	
}
