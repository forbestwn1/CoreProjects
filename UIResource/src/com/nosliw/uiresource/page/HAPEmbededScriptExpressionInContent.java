package com.nosliw.uiresource.page;

import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.uiresource.expression.HAPEmbededScriptExpression;
import com.nosliw.uiresource.expression.HAPScriptExpression;

/*
 * class for script expression part in html content
 */
public class HAPEmbededScriptExpressionInContent extends HAPEmbededScriptExpression{

	public HAPEmbededScriptExpressionInContent(String uiId, HAPScriptExpression scriptExpression, HAPExpressionSuiteManager expressionManager){
		super(uiId, scriptExpression, expressionManager);
	}

}
