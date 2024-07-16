package com.nosliw.core.application.common.scriptexpression;

import com.nosliw.common.container.HAPContainer;

public class HAPContainerScriptExpression extends HAPContainer<HAPItemInContainerScriptExpression>{

	public String addScriptExpression(HAPExpressionScript scriptExpression) {
		HAPItemInContainerScriptExpression item = new HAPItemInContainerScriptExpression(scriptExpression);
		return this.addItem(item);
	}
}
