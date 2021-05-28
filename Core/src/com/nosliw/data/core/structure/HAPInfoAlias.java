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

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HAPInfoAlias) {
			return ((HAPInfoAlias)obj).getName().equals(this.getName());
		}
		else if(obj instanceof String) {
			return obj.equals(this.getName());
		}
		return false;
	}
	
}
