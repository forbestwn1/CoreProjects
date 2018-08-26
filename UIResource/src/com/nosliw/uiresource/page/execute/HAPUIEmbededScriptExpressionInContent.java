package com.nosliw.uiresource.page.execute;

import java.util.List;

import com.nosliw.data.core.script.expressionscript.HAPScriptExpression;

/*
 * class for script expression part in html content
 */
public class HAPUIEmbededScriptExpressionInContent extends HAPUIEmbededScriptExpression{

	public HAPUIEmbededScriptExpressionInContent(String uiId, HAPScriptExpression scriptExpression){
		super(uiId, scriptExpression);
	}

	public HAPUIEmbededScriptExpressionInContent(String uiId, List<Object> elements){
		super(uiId, elements);
	}
	
	public HAPUIEmbededScriptExpressionInContent(String uiId, String content){
		super(uiId, content);
	}
}
