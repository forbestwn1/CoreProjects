package com.nosliw.uiresource;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.uiresource.expression.HAPScriptExpression;

/*
 * class for script expression part in html content
 */
@HAPEntityWithAttribute
public class HAPEmbededScriptExpressionInContent extends HAPEmbededScriptExpression{

	public HAPEmbededScriptExpressionInContent(HAPScriptExpression scriptExpression){
		super(scriptExpression.getId(), scriptExpression);
	}
	
	
}
