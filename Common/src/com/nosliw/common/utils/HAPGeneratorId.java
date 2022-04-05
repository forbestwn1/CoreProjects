package com.nosliw.common.utils;

public class HAPGeneratorId {

	private int m_idIndex = 0;
	
	public HAPGeneratorId(int initIndex){
		this.m_idIndex = initIndex;
	}

	public HAPGeneratorId(){
	}

	public String generateId(){		
		String out = String.valueOf(++this.m_idIndex);
		return out;
	}

}
