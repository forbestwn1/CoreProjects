package com.nosliw.uiresource.page.definition;

import com.nosliw.data.core.script.expression.HAPDefinitionScriptExpression;

public class HAPDefinitionUIEmbededScriptExpressionInContent extends HAPDefinitionUIEmbededScriptExpression{

	public HAPDefinitionUIEmbededScriptExpressionInContent(String uiId, HAPDefinitionScriptExpression scriptExpression) {
		super(uiId, scriptExpression);
	}

	public HAPDefinitionUIEmbededScriptExpressionInContent(String uiId, String content) {
		super(uiId, content);
	}
	
}
