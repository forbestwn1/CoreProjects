package com.nosliw.data.core.structure;

public class HAPInfoAlias {

	private String m_name;
	
	//lower the number, higher the priority
	private double m_priority;

	public HAPInfoAlias(String name, double priority) {
		this.m_name = name;
		this.m_priority = priority;
	}
	
	public String getName() {   return this.m_name;    }
	
	public double getPriority() {   return this.m_priority;   }
	
}
