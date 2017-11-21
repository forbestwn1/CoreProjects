package com.nosliw.uiresource;

public class HAPIdGenerator {

	private int m_idIndex = 0;
	
	public HAPIdGenerator(int initIndex){
		this.m_idIndex = initIndex;
	}

	public HAPIdGenerator(){
	}

	public String createId(){		return String.valueOf(++this.m_idIndex);	}

}
