package com.nosliw.common.updatename;

public class HAPUpdateNamePrefix implements HAPUpdateName{

	private String m_prefix;
	
	public HAPUpdateNamePrefix(String prefix) {
		this.m_prefix = prefix;
	}
	
	@Override
	public String getUpdatedName(String name) {
		return this.m_prefix + name;
	}
}
