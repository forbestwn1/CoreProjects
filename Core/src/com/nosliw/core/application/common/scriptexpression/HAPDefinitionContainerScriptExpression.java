package com.nosliw.core.application.common.scriptexpression;

import com.nosliw.common.container.HAPContainer;

public class HAPDefinitionContainerScriptExpression extends HAPContainer<HAPDefinitionScriptExpressionItemInContainer>{

	public String addScriptExpression(HAPDefinitionScriptExpression scriptExpression) {
		return this.addItem(new HAPDefinitionScriptExpressionItemInContainer(scriptExpression));
	}
	
	public String addScriptExpression(String scriptExpression) {
		return this.addItem(new HAPDefinitionScriptExpressionItemInContainer(new HAPDefinitionScriptExpression(scriptExpression)));
	}
}
