package com.nosliw.uiresource.page;

import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.script.expressionscript.HAPEmbededScriptExpression;
import com.nosliw.data.core.script.expressionscript.HAPScriptExpression;

/*
 * class for script expression part in html content
 */
public class HAPEmbededScriptExpressionInContent extends HAPEmbededScriptExpressionElement{

	public HAPEmbededScriptExpressionInContent(String uiId, HAPScriptExpression scriptExpression, HAPExpressionSuiteManager expressionManager){
		super(uiId, scriptExpression, expressionManager);
	}

}
