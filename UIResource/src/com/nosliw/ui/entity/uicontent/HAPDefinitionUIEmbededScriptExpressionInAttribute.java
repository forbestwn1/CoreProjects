package com.nosliw.ui.entity.uicontent;

public class HAPDefinitionUIEmbededScriptExpressionInAttribute extends HAPDefinitionUIEmbededScriptExpression{

	//attribute name
	private String m_attribute;

	public HAPDefinitionUIEmbededScriptExpressionInAttribute(String attribute, String uiId, String scriptId) {
		super(uiId, scriptId);
		this.m_attribute = attribute;
	}

	public String getAttribute() {		return this.m_attribute;	}
	
}