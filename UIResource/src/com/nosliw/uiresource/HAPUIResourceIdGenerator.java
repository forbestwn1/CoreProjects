package com.nosliw.uiresource;

public class HAPUIResourceIdGenerator {

	private int m_idIndex = 0;
	
	public HAPUIResourceIdGenerator(int initIndex){
		this.m_idIndex = initIndex;
	}

	public HAPUIResourceIdGenerator(){
	}

	public String createId(){		return String.valueOf(++this.m_idIndex);	}

}
