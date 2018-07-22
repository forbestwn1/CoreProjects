package com.nosliw.uiresource.page;

import com.nosliw.data.core.expressionscript.HAPEmbededScriptExpression;
import com.nosliw.data.core.expressionscript.HAPScriptExpression;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;

/*
 * class for script expression part in html content
 */
public class HAPEmbededScriptExpressionInContent extends HAPEmbededScriptExpressionElement{

	public HAPEmbededScriptExpressionInContent(String uiId, HAPScriptExpression scriptExpression, HAPExpressionSuiteManager expressionManager){
		super(uiId, scriptExpression, expressionManager);
	}

}
