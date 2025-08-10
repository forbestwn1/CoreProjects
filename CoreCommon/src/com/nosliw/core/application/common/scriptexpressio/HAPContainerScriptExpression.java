package com.nosliw.core.application.common.scriptexpressio;

import com.nosliw.common.container.HAPContainer;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;

public class HAPContainerScriptExpression extends HAPContainer<HAPItemInContainerScriptExpression>{

	public String addScriptExpression(HAPExpressionScript scriptExpression) {
		HAPItemInContainerScriptExpression item = new HAPItemInContainerScriptExpression(scriptExpression);
		return this.addItem(item);
	}
}
