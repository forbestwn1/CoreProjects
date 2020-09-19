package com.nosliw.data.core.story;

import com.nosliw.common.utils.HAPConstant;

public class HAPAliasElement implements HAPReferenceElement{

	private String m_name;
	
	public HAPAliasElement(String alias) {
		this.m_name = alias;
	}
	
	public String getAlias() {
		return this.m_name;
	}

	@Override
	public String getEntityOrReferenceType() {  return HAPConstant.REFERENCE;  }
	
}
