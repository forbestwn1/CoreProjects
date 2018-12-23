package com.nosliw.uiresource.page.definition;

import java.util.List;

import com.nosliw.data.core.script.expression.HAPDefinitionScriptExpressionEmbeded;

public class HAPDefinitionUIEmbededScriptExpressionInAttribute extends HAPDefinitionUIEmbededScriptExpression{

	//attribute name
	private String m_attribute;

	public HAPDefinitionUIEmbededScriptExpressionInAttribute(String attribute, String uiId, HAPDefinitionScriptExpressionEmbeded scriptExpression) {
		super(uiId, scriptExpression);
		this.m_attribute = attribute;
	}

	public HAPDefinitionUIEmbededScriptExpressionInAttribute(String attribute, String uiId, String content) {
		super(uiId, content);
		this.m_attribute = attribute;
	}

	public HAPDefinitionUIEmbededScriptExpressionInAttribute(String uiId, String attribute, List<Object> elements) {
		super(uiId, elements);
		this.m_attribute = attribute;
	}

	public String getAttribute() {		return this.m_attribute;	}
	
}
