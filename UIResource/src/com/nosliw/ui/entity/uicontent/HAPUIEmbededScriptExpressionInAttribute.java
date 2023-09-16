package com.nosliw.ui.entity.uicontent;

public class HAPUIEmbededScriptExpressionInAttribute extends HAPUIEmbededScriptExpression{

	//attribute name
	private String m_attribute;

	public HAPUIEmbededScriptExpressionInAttribute(String attribute, String uiId, String scriptId) {
		super(uiId, scriptId);
		this.m_attribute = attribute;
	}

	public String getAttribute() {		return this.m_attribute;	}
	
}
