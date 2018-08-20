package com.nosliw.uiresource.page.definition;

import java.util.List;

import com.nosliw.data.core.script.expressionscript.HAPDefinitionEmbededScript;

public class HAPDefinitionUIEmbededScriptExpressionInAttribute extends HAPDefinitionUIEmbededScriptExpression{

	//attribute name
	private String m_attribute;

	public HAPDefinitionUIEmbededScriptExpressionInAttribute(String uiId, String attribute, HAPDefinitionEmbededScript scriptExpression) {
		super(uiId, scriptExpression);
		this.m_attribute = attribute;
	}

	public HAPDefinitionUIEmbededScriptExpressionInAttribute(String uiId, String attribute, String content) {
		super(uiId, content);
		this.m_attribute = attribute;
	}

	public HAPDefinitionUIEmbededScriptExpressionInAttribute(String uiId, String attribute, List<Object> elements) {
		super(uiId, elements);
		this.m_attribute = attribute;
	}

	public String getAttribute() {		return this.m_attribute;	}
	
}
