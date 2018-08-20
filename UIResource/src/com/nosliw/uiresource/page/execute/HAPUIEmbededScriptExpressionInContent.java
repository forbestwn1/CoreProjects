package com.nosliw.uiresource.page.execute;

import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.script.expressionscript.HAPEmbededScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpression;

/*
 * class for script expression part in html content
 */
public class HAPUIEmbededScriptExpressionInContent extends HAPUIEmbededScriptExpression{

	public HAPUIEmbededScriptExpressionInContent(String uiId, HAPScriptExpression scriptExpression){
		super(uiId, scriptExpression);
	}

}
