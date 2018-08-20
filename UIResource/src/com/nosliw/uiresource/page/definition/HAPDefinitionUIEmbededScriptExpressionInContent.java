package com.nosliw.uiresource.page.definition;

import java.util.List;

import com.nosliw.data.core.script.expressionscript.HAPDefinitionEmbededScript;

public class HAPDefinitionUIEmbededScriptExpressionInContent extends HAPDefinitionUIEmbededScriptExpression{

	public HAPDefinitionUIEmbededScriptExpressionInContent(String uiId, HAPDefinitionEmbededScript scriptExpression) {
		super(uiId, scriptExpression);
	}

	public HAPDefinitionUIEmbededScriptExpressionInContent(String uiId, String content) {
		super(uiId, content);
	}
	
	public HAPDefinitionUIEmbededScriptExpressionInContent(String uiId, List<Object> elements) {
		super(uiId, elements);
	}

}
