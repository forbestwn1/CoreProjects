package com.nosliw.uiresource.page.definition;

public class HAPDefinitionUIEmbededScriptExpressionInAttribute extends HAPDefinitionUIEmbededScriptExpression{

	//attribute name
	private String m_attribute;

	public HAPDefinitionUIEmbededScriptExpressionInAttribute(String attribute, String uiId, String content) {
		super(uiId, content);
		this.m_attribute = attribute;
	}

	public String getAttribute() {		return this.m_attribute;	}
	
}
