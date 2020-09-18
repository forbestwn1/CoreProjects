package com.nosliw.data.core.story;

public class HAPAlias implements HAPReferenceElement{

	private String m_alias;
	
	public HAPAlias(String alias) {
		this.m_alias = alias;
	}
	
	public String getAlias() {
		return this.m_alias;
	}
	
}
